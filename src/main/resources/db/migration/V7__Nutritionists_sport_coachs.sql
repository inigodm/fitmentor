CREATE TABLE IF NOT EXISTS nutritionists (
        id UUID PRIMARY KEY,
        user_Id UUID
    );

CREATE TABLE IF NOT EXISTS fitness (
        id UUID PRIMARY KEY,
        user_Id UUID
);