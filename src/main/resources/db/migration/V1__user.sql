CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    role VARCHAR NOT NULL
);

INSERT INTO users (id, username, password, email, role)
values
('00000000-0000-0000-0000-000000000001', 'admin', 'admin', 'admin@admin.com', 42);