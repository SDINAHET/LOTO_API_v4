INSERT INTO users (email, password, role, enabled)
VALUES (
    'test4@hbnb.com',
    '$2y$10$t.GnOZY8W.4eC4D29AUFz.LyDO91UUpgDk8XSiZ6J1SRqgInfnGFK',
    'ROLE_USER',
    true
)
ON CONFLICT (email) DO NOTHING;
