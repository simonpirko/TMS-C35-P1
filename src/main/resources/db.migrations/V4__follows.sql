create table if not exists subscriptions
(
    id           serial
        constraint subscriptions_pk
            primary key,
    follower_id  integer not null
        references accounts
            on delete cascade,
    following_id integer not null
        references accounts
            on delete cascade,
    constraint unique_follow
        unique (follower_id, following_id),
    constraint subscriptions_check
        check (follower_id <> following_id)
);