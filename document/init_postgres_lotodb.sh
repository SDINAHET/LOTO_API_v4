#!/bin/bash
set -e

DB_NAME="lotodb"
DB_USER="postgres"
SCHEMA_FILE="./schema_lotodb.sql"

echo "=== Init PostgreSQL: $DB_NAME ==="

# Démarrer PostgreSQL (WSL/Ubuntu)
sudo service postgresql start

# Vérifier que le fichier SQL existe
if [ ! -f "$SCHEMA_FILE" ]; then
  echo "❌ Fichier introuvable: $SCHEMA_FILE"
  exit 1
fi

# Créer la DB si besoin
if ! sudo -u "$DB_USER" psql -lqt | cut -d \| -f 1 | tr -d ' ' | grep -qw "$DB_NAME"; then
  echo "📦 Création de la base $DB_NAME..."
  sudo -u "$DB_USER" createdb "$DB_NAME"
else
  echo "✅ La base $DB_NAME existe déjà."
fi

# Appliquer le schéma
echo "🚀 Application du schéma..."
sudo -u "$DB_USER" psql -d "$DB_NAME" -f "$SCHEMA_FILE"

echo "✅ Terminé ! DB=$DB_NAME"
