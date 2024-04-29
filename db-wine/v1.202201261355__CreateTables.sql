create table winery(
    id int UNSIGNED NOT NULL AUTO_INCREMENT,
    name nvarchar(255) NOT NULL,
    location nvarchar(255) NOT NULL,
    CONSTRAINT winery_pk PRIMARY KEY (id),
    UNIQUE KEY winery_uk (name, location)
);
create table wines(
    id int UNSIGNED NOT NULL AUTO_INCREMENT,
    winery_id int UNSIGNED NOT NULL,
    name nvarchar(255) NOT NULL,
    style nvarchar(255) NOT NULL,
    CONSTRAINT wines_pk PRIMARY KEY (id)
);
create table wine_notes(
    id int UNSIGNED NOT NULL AUTO_INCREMENT,
    wine_id int UNSIGNED NOT NULL,
    user nvarchar(255) NOT NULL,
    note nvarchar(255) NOT NULL,
    ordinal int UNSIGNED NOT NULL,
    CONSTRAINT wine_note_pk PRIMARY KEY (id)
);
create table wine_rating(
    wine_id int UNSIGNED NOT NULL,
    user nvarchar(255) NOT NULL,
    date nvarchar(255) NOT NULL,
    rating nvarchar(255) NOT NULL,
    CONSTRAINT wine_rating_pk PRIMARY KEY (wine_id, user, date)
);
