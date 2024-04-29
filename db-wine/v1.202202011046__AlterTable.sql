alter table winery add column id_str nvarchar(255);
update winery set id_str = id;
alter table winery modify id int not null;
alter table winery drop primary key;
alter table winery drop id;
alter table winery rename column id_str to id;

alter table wines add column id_str nvarchar(255);
update wines set id_str = id;
alter table wines modify id int not null;
alter table wines drop primary key;
alter table wines drop id;
alter table wines rename column id_str to id;

alter table wine_notes modify id int not null;
alter table wine_notes drop primary key;
alter table wine_notes drop id;
