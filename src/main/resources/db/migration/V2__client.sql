CREATE TABLE clients (
    id UUID PRIMARY KEY,
    goals VARCHAR NOT NULL,
    age INT NOT NULL,
    injuries VARCHAR NOT NULL,
    weight INT NOT NULL,
    equipment_access INT NOT NULL,
    prefered_training_style VARCHAR NOT NULL,
    phonenumber VARCHAR NOT NULL,
    userId UUID NOT NULL,
    coachId UUID
);

CREATE INDEX idx_clients_userId ON clients (userId);