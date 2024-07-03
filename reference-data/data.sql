create schema reference_data;

create table reference_data.weapon
(
    id bigint not null constraint weapon_pk primary key,
    weapon_name varchar(30) not null unique
);

insert into reference_data.weapon(id, weapon_name) values (0, 'Desert eagle');
insert into reference_data.weapon(id, weapon_name) values (1, 'AK-47');
insert into reference_data.weapon(id, weapon_name) values (2, 'Knife');
insert into reference_data.weapon(id, weapon_name) values (3, 'AWP');