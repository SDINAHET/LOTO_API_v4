#!/bin/bash
set -euo pipefail

DB_NAME="${POSTGRES_DB:-lotodb}"
DB_USER="${POSTGRES_USER:-postgres}"
DB_PASS="${POSTGRES_PASSWORD:-postgres}"

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
SCHEMA_FILE="${SCRIPT_DIR}/sql/schema_lotodb.sql"
DATA_DIR="${SCRIPT_DIR}/data"

USERS_CSV="${DATA_DIR}/users_export.csv"
TICKETS_CSV="${DATA_DIR}/tickets_export.csv"
GAINS_CSV="${DATA_DIR}/ticket_gains_export.csv"

echo "=== PostgreSQL init: schema + data ==="
sudo service postgresql start

# Vérifs CSV
for f in "$USERS_CSV" "$TICKETS_CSV" "$GAINS_CSV"; do
  if [ ! -f "$f" ]; then
    echo "❌ CSV manquant: $f"
    echo "👉 Attendu:"
    echo " - init/data/users_export.csv"
    echo " - init/data/tickets_export.csv"
    echo " - init/data/ticket_gains_export.csv"
    exit 1
  fi
done

# Créer user + db (idempotent)
sudo -u postgres psql -v ON_ERROR_STOP=1 <<SQL
DO \$\$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = '${DB_USER}') THEN
    CREATE ROLE ${DB_USER} LOGIN PASSWORD '${DB_PASS}';
  END IF;
END
\$\$;

DO \$\$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_database WHERE datname = '${DB_NAME}') THEN
    CREATE DATABASE ${DB_NAME} OWNER ${DB_USER};
  END IF;
END
\$\$;
SQL

echo "✅ DB=$DB_NAME USER=$DB_USER"

# 1) Appliquer le schéma si le fichier existe
if [ -f "$SCHEMA_FILE" ]; then
  echo "🚀 Application du schéma depuis: $SCHEMA_FILE"
  sudo -u postgres psql -d "$DB_NAME" -v ON_ERROR_STOP=1 -f "$SCHEMA_FILE"
else
  echo "⚠️ Aucun schema_lotodb.sql trouvé -> création du schéma inline"
  sudo -u postgres psql -d "$DB_NAME" -v ON_ERROR_STOP=1 <<'SQL'
BEGIN;

CREATE TABLE IF NOT EXISTS public.users (
  id text PRIMARY KEY,
  first_name text NOT NULL,
  last_name text NOT NULL,
  email text NOT NULL UNIQUE,
  password text NOT NULL,
  is_admin boolean DEFAULT false NOT NULL,
  created_at text,
  updated_at text
);

CREATE TABLE IF NOT EXISTS public.tickets (
  id text PRIMARY KEY,
  numbers text NOT NULL,
  lucky_number integer NOT NULL,
  draw_date text NOT NULL,
  draw_day text,
  created_at text NOT NULL,
  updated_at text,
  user_id text NOT NULL
);

CREATE TABLE IF NOT EXISTS public.ticket_gains (
  id text PRIMARY KEY,
  ticket_id text NOT NULL,
  matching_numbers integer NOT NULL,
  lucky_number_match boolean NOT NULL,
  gain_amount real NOT NULL
);

-- FK gains -> tickets
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='ticket_gains_ticket_id_fkey') THEN
    ALTER TABLE public.ticket_gains
      ADD CONSTRAINT ticket_gains_ticket_id_fkey
      FOREIGN KEY (ticket_id) REFERENCES public.tickets(id)
      ON UPDATE CASCADE ON DELETE CASCADE;
  END IF;
END $$;

-- (Recommandé) FK tickets -> users
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='tickets_user_id_fkey') THEN
    ALTER TABLE public.tickets
      ADD CONSTRAINT tickets_user_id_fkey
      FOREIGN KEY (user_id) REFERENCES public.users(id)
      ON UPDATE CASCADE ON DELETE CASCADE;
  END IF;
END $$;

COMMIT;
SQL
fi

# 2) Reset tables (rejouable) – ordre important
echo "🧹 TRUNCATE tables..."
sudo -u postgres psql -d "$DB_NAME" -v ON_ERROR_STOP=1 <<'SQL'
BEGIN;
TRUNCATE TABLE public.ticket_gains;
TRUNCATE TABLE public.tickets;
TRUNCATE TABLE public.users;
COMMIT;
SQL

# 3) Import CSV (pas de header, delimiter ;)
echo "📥 Import users..."
sudo -u postgres psql -d "$DB_NAME" -v ON_ERROR_STOP=1 <<SQL
\copy public.users(id, first_name, last_name, email, password, is_admin, created_at, updated_at)
FROM '$USERS_CSV'
WITH (FORMAT csv, DELIMITER ';');
SQL

echo "📥 Import tickets..."
sudo -u postgres psql -d "$DB_NAME" -v ON_ERROR_STOP=1 <<SQL
\copy public.tickets(id, numbers, lucky_number, draw_date, draw_day, created_at, updated_at, user_id)
FROM '$TICKETS_CSV'
WITH (FORMAT csv, DELIMITER ';');
SQL

echo "📥 Import ticket_gains..."
sudo -u postgres psql -d "$DB_NAME" -v ON_ERROR_STOP=1 <<SQL
\copy public.ticket_gains(id, ticket_id, matching_numbers, lucky_number_match, gain_amount)
FROM '$GAINS_CSV'
WITH (FORMAT csv, DELIMITER ';');
SQL

echo "✅ Vérification:"
sudo -u postgres psql -d "$DB_NAME" -c \
"SELECT 'users' t, count(*) FROM public.users
 UNION ALL SELECT 'tickets', count(*) FROM public.tickets
 UNION ALL SELECT 'ticket_gains', count(*) FROM public.ticket_gains;"

echo "✅ Init PostgreSQL terminé (schema + data)."
