CREATE TABLE posts (
 id SERIAL PRIMARY KEY,
 title VARCHAR(200) NOT NULL,
 content TEXT NOT NULL,
 created_at TIMESTAMP DEFAULT now()
);

alter table posts
    owner to postgres;