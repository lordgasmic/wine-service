create table wine_image_thumbnail(
    id int UNSIGNED NOT NULL AUTO_INCREMENT,
    wine_id int UNSIGNED NOT NULL,
    label varchar(255),
    thumbnail MEDIUMBLOB,
    mime_type varchar(255),
    CONSTRAINT wine_image_pk PRIMARY KEY (id)
);
