create table comments
(
    postId INT NOT NULL,
    commentAuthor VARCHAR NOT NULL,
    commentText INTEGER NOT NULL,

    CONSTRAINT comments_pk PRIMARY KEY (postId),

    CONSTRAINT comments_posts_id_fk FOREIGN KEY (postId)
        REFERENCES posts(id)
        ON DELETE CASCADE
);