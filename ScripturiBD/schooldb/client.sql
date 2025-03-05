create or replace table client
(
	id int auto_increment
		constraint `PRIMARY`
			primary key,
	nume varchar(45) null,
	tel varchar(45) null,
	adresa varchar(45) null
)
auto_increment = 5619563;

