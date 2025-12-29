CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR NOT NULL UNIQUE,
    password VARCHAR NOT NULL,
    email VARCHAR NOT NULL UNIQUE,
    role VARCHAR NOT NULL,
    fido2_credential_id VARCHAR,
    fido2_public_key VARCHAR,
    fido2_counter INTEGER,
    current_challenge VARCHAR,
    challenge_expiry DATE
);

INSERT INTO users (id, username, password, email, role)
values
('00000000-0000-0000-0000-000000000001', 'admin', 'admin', 'admin@admin.com', 42);