create table author
(
    id     serial primary key,
    fio    varchar(255)  not null,
    creation_date_time timestamp not null

);