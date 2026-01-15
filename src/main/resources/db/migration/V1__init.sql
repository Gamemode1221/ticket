create table if not exists app_user (
    id bigserial primary key,
    username varchar(50) not null,
    password_hash varchar(255) not null,
    role varchar(20) not null,
    constraint uk_app_user_username unique (username)
);

create table if not exists ticket (
    id bigserial primary key,
    title varchar(255) not null,
    content text not null,
    status varchar(50) not null,
    created_at timestamp not null default now()
);