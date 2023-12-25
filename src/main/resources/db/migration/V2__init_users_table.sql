create table users(
    id bigserial not null primary key,
    name varchar(255) not null,
    surname varchar(255) not null,
    birth_date timestamp not null,
    email varchar(255) not null,
    password varchar(255) not null,
    license varchar(255) null,
    role varchar(255) not null,
    create_date timestamp null,
    modify_date timestamp null,
    deleted boolean default false not null
);