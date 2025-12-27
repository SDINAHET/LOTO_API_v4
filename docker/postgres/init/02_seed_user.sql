INSERT INTO users (email, password, role, enabled)
VALUES (
    'test4@hbnb.com',
    '$2a$10$xeJc3OOp/XrF/s5oMZkM7u8cIzvaMLCezcWlI/NnTBNVDRQXrAaa2',
    'ROLE_ADMIN',
    true
)
ON CONFLICT (email) DO NOTHING;
