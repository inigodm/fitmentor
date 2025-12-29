CREATE TABLE IF NOT EXISTS supplements (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255)
);

INSERT INTO supplements (id, name, description) VALUES
    ('b1111111-1111-1111-1111-111111111111', 'Creatine', 'Supplement for strength and recovery'),
    ('b2222222-2222-2222-2222-222222222222', 'Proteín Whey', 'Whey protein supplement'),
    ('b3333333-3333-3333-3333-333333333333', 'BCAA', 'Branched-chain amino acids');
