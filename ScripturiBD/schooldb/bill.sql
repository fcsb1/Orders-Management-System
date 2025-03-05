create or replace table bill
(
	numarulFacturii int not null
		constraint `PRIMARY`
			primary key,
	data varchar(45) null,
	clientID int not null,
	productID int not null
);

