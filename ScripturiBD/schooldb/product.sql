create or replace table product
(
	id int default 0 not null
		constraint `PRIMARY`
			primary key,
	denumire varchar(45) null,
	price int null,
	stock int default 0 null
);

