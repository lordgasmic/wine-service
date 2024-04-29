create or replace view wine_image_vw as select * from wine_image;
grant select, insert, update, delete on web.wine_image_vw to 'web_appl'@'localhost';
grant select, insert, update, delete on web.wine_image_vw to 'web_appl'@'%';
