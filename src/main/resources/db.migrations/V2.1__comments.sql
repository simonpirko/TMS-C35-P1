create table comments
(
    id            serial
        constraint comments_pk
            primary key,
    commentAuthor varchar not null,
    postId INT not null,
    commentText   integer not null
);