CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- CREATE TABLE IF NOT EXISTS users (
--     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
--     email VARCHAR(255) NOT NULL UNIQUE,
--     password VARCHAR(255) NOT NULL,
--     role VARCHAR(50) NOT NULL,
--     enabled BOOLEAN DEFAULT TRUE
-- );

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
