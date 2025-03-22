INSERT INTO hruser (email, password)
VALUES ('hr@company.com', 'password')
    ON CONFLICT (email) DO NOTHING;