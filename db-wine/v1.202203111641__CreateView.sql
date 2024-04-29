create or replace view wine_image_thumbnail_vw as select * from wine_image_thumbnail;
grant select, insert, update, delete on web.wine_image_thumbnail_vw to 'web_appl'@'localhost';
grant select, insert, update, delete on web.wine_image_thumbnail_vw to 'web_appl'@'%';
