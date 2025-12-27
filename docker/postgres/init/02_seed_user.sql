-- INSERT INTO users (email, password, role, enabled)
-- VALUES (
--     'test4@hbnb.com',
--     '$2a$10$xeJc3OOp/XrF/s5oMZkM7u8cIzvaMLCezcWlI/NnTBNVDRQXrAaa2',
--     'ROLE_ADMIN',
--     true
-- )
-- ON CONFLICT (email) DO NOTHING;

INSERT INTO public.users (id, first_name, last_name, email, password, is_admin, created_at, updated_at)
VALUES (
  'ci-user-001',
  'CI',
  'User',
  'test4@hbnb.com',
  '$2a$10$xeJc3OOp/XrF/s5oMZkM7u8cIzvaMLCezcWlI/NnTBNVDRQXrAaa2',
  true,
  to_char(now(), 'YYYY-MM-DD"T"HH24:MI:SS'),
  to_char(now(), 'YYYY-MM-DD"T"HH24:MI:SS')
)
ON CONFLICT (email) DO NOTHING;
