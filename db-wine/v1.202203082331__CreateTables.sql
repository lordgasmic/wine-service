create table wine_image(
    id int UNSIGNED NOT NULL AUTO_INCREMENT,
    wine_id int UNSIGNED NOT NULL,
    front blob,
    back blob,
    CONSTRAINT wine_image_pk PRIMARY KEY (id)
);
