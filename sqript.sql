CREATE TABLE users
(   id bigserial primary key,
    username VARCHAR(128) unique,
    firstname VARCHAR(128),
    lastname VARCHAR(128),
    birth_date DATE,
    age INT,
    role varchar(32),
    info jsonb,
    company_id int references company (id)
);

create table users_chat
(
  id bigserial primary key ,
  user_id bigint references users(id),
  chat_id bigint references chat(id),
  created_at timestamp not null,
  created_by varchar(128) not null,
  unique (user_id, chat_id)
);

drop table users_chat;

create table company_locale
(
    company_id int not null references company(id),
    lang char(2) not null ,
    description varchar(128) not null ,
    primary key (company_id, lang)
);


create table chat
(
  id bigserial primary key,
  name varchar(64) not null unique
);

create table profile
(
    id bigserial primary key ,
    user_id bigint not null unique references users(id),
    street varchar(128),
    language char(2)
);

drop table profile;



create table company
(
    id serial primary key,
    name varchar(64) not null
);


drop table users;

drop table company;