CREATE TABLE IF NOT EXISTS account_details (
     account_id INTEGER PRIMARY KEY REFERENCES accounts(id) ON DELETE CASCADE,
     email VARCHAR(255),
     bio TEXT,
     location VARCHAR(100),
     website VARCHAR(255),
     birth_date DATE,
     avatar_url VARCHAR(500),
     header_url VARCHAR(500)
);

alter table account_details
    owner to postgres;