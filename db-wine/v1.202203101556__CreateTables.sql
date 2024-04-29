create table wine_image(
    id int UNSIGNED NOT NULL AUTO_INCREMENT,
    wine_id int UNSIGNED NOT NULL,
    label varchar(255),
    image MEDIUMBLOB,
    mime_type varchar(255),
    CONSTRAINT wine_image_pk PRIMARY KEY (id)
);
