-- schema_lotodb.sql

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

-- FK ticket_gains -> tickets (déjà dans ton dump)
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_constraint WHERE conname = 'ticket_gains_ticket_id_fkey'
  ) THEN
    ALTER TABLE public.ticket_gains
      ADD CONSTRAINT ticket_gains_ticket_id_fkey
      FOREIGN KEY (ticket_id) REFERENCES public.tickets(id)
      ON UPDATE CASCADE ON DELETE CASCADE;
  END IF;
END $$;

-- ✅ BONUS (recommandé) : FK tickets -> users (tu ne l’avais pas dans le dump)
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_constraint WHERE conname = 'tickets_user_id_fkey'
  ) THEN
    ALTER TABLE public.tickets
      ADD CONSTRAINT tickets_user_id_fkey
      FOREIGN KEY (user_id) REFERENCES public.users(id)
      ON UPDATE CASCADE ON DELETE CASCADE;
  END IF;
END $$;

COMMIT;
