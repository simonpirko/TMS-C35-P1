CREATE TABLE IF NOT EXISTS accounts (
  id SERIAL PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL
);

alter table accounts
    owner to postgres;

