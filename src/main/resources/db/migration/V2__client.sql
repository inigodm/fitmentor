CREATE TABLE clients (
    id UUID PRIMARY KEY,
    goals VARCHAR NOT NULL,
    age INT NOT NULL,
    injuries VARCHAR NOT NULL,
    weight INT NOT NULL,
    equipment_access INT NOT NULL,
    phone_number VARCHAR NOT NULL,
    user_id UUID NOT NULL
);

CREATE INDEX idx_clients_userId ON clients (user_id);