create table if not exists dbmodelartist (

    id serial,
    name varchar(20) not null,
    primary key (id)

);

create table if not exists dbmodelalbum (

    id serial,
    artistid int not null,
    name varchar(20) not null,
    year int not null,
    tracks int not null,
    primary key (id),
    foreign key (artistid) references dbmodelartist(id) on delete cascade

);