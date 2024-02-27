CREATE TABLE IF NOT EXISTS news
(
    id    SERIAL PRIMARY KEY,
    time  TIMESTAMP    NOT NULL,
    title VARCHAR(255) NOT NULL,
    text  TEXT         NOT NULL
);