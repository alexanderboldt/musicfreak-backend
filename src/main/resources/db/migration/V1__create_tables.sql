create table if not exists artistentity (
    id serial primary key,
    name varchar(20) not null,
    createdAt timestamp not null,
    updatedAt timestamp not null
);

CREATE SEQUENCE artistentity_seq START 1 INCREMENT 1;

create table if not exists albumentity (
    id serial primary key,
    artistid int not null,
    name varchar(20) not null,
    year int not null,
    tracks int not null,
    createdAt timestamp not null,
    updatedAt timestamp not null,
    foreign key (artistid) references artistentity(id) on delete cascade
);

CREATE SEQUENCE albumentity_seq START 1 INCREMENT 1;