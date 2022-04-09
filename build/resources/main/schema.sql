CREATE TABLE IF NOT EXISTS USER
(
    id           UUID PRIMARY KEY,
    user_name    VARCHAR(255) NOT NULL,
    first_name   VARCHAR(255),
    last_name    VARCHAR(255),
    street       VARCHAR(255),
    city         VARCHAR(255),
    postal_code  VARCHAR(255),
    country_code VARCHAR(255)
    );

CREATE INDEX user_name ON user(user_name);

