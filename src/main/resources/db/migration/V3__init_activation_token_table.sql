ALTER TABLE users ADD activated boolean NOT NULL DEFAULT false;

create table activation_token(
    id bigserial not null primary key,
    token varchar(255) not null,
    expired_at timestamp not null,
    confirmed boolean default false,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);