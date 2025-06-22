CREATE TABLE coaches (
    id UUID PRIMARY KEY,
    presentation VARCHAR NOT NULL,
    photo VARCHAR NOT NULL,
    phone_number VARCHAR NOT NULL,
    user_id UUID NOT NULL
);

CREATE INDEX idx_coaches_userId ON coaches (user_id);