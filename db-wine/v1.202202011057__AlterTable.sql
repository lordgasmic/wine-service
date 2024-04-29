alter table wine_notes add column id nvarchar(255);
alter table wine_rating add column id nvarchar(255);

alter table wine_notes modify column id int auto_increment primary key;
alter table wine_rating modify column id int auto_increment primary key;

alter table wine_notes add column id_str nvarchar(255);
alter table wine_rating add column id_str nvarchar(255);

update wine_notes set id_str = id;
update wine_rating set id_str = id;

alter table wine_notes modify column id int not null;
alter table wine_notes drop primary key;
alter table wine_notes drop column id;
alter table wine_notes rename column id_str to id;
alter table wine_notes modify column id nvarchar(255) not null;
alter table wine_rating modify column id int not null;
alter table wine_rating drop primary key;
alter table wine_rating drop column id;
alter table wine_rating rename column id_str to id;
alter table wine_rating modify column id nvarchar(255) not null;
