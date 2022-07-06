create table if not exists Discounted_Wines (
    id int not null,
    site varchar(50) not null,
    name varchar(50) not null,
    alias varchar(50),
    price double not null,
    priceWithDiscount double not null,
    discount double not null,
    image varchar(255) not null,

    primary key(id)
);