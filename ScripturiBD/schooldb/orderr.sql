create or replace table orderr
(
	noOrder int not null
		constraint `PRIMARY`
			primary key,
	clientId int not null,
	productId int not null,
	quantity int null,
	price int default 0 null
);

