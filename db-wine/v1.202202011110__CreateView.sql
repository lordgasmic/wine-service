create or replace view winery_vw as select * from winery;
grant select, insert, update, delete on web.winery_vw to 'web_appl'@'localhost';
grant select, insert, update, delete on web.winery_vw to 'web_appl'@'%';

create or replace view wines_vw as select * from wines;
grant select, insert, update, delete on web.wines_vw to 'web_appl'@'localhost';
grant select, insert, update, delete on web.wines_vw to 'web_appl'@'%';

create or replace view wine_notes_vw as select * from wine_notes;
grant select, insert, update, delete on web.wine_notes_vw to 'web_appl'@'localhost';
grant select, insert, update, delete on web.wine_notes_vw to 'web_appl'@'%';

create or replace view wine_rating_vw as select * from wine_rating;
grant select, insert, update, delete on web.wine_rating_vw to 'web_appl'@'localhost';
grant select, insert, update, delete on web.wine_rating_vw to 'web_appl'@'%';
