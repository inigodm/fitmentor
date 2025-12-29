CREATE TABLE plans (
    id UUID PRIMARY KEY,
    active BOOLEAN NOT NULL,
    client UUID NOT NULL,
    coach UUID NOT NULL,
    description VARCHAR NOT NULL,
    type VARCHAR NOT NULL,
    equipment VARCHAR NOT NULL,
    goals VARCHAR NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_plans_client ON plans(client);
CREATE INDEX idx_plans_coach ON plans(coach);

CREATE TABLE timeslots (
    id UUID PRIMARY KEY,
    client UUID NOT NULL,
    coach UUID NOT NULL,
    plan UUID NOT NULL,
    day_of_week INT NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);

CREATE INDEX idx_timeslots_plan ON timeslots (plan);
CREATE INDEX idx_timeslots_client ON timeslots (client);
CREATE INDEX idx_timeslots_coach ON timeslots (coach);