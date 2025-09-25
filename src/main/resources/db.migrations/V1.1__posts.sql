CREATE TABLE IF NOT EXISTS posts (
 id SERIAL PRIMARY KEY,
 title VARCHAR(200) NOT NULL,
 content TEXT NOT NULL,
user_id INTEGER,
 created_at TIMESTAMP DEFAULT now(),
    likes integer
);

alter table posts
    owner to postgres;