DROP TABLE IF EXISTS album_scheduler_task;

CREATE TABLE album_scheduler_task (started_at TIMESTAMP NULL DEFAULT NULL);

INSERT INTO album_scheduler_task (started_at) VALUES (NULL);

drop table album;

create table album (
id bigint(20) primary key auto_increment,
artist varchar(255),
rating int(11) not null,
title varchar(255),
year int(11) not null
)