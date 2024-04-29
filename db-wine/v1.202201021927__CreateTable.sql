create table wine_rating(
    wine_id int UNSIGNED NOT NULL,
    user nvarchar(255) NOT NULL,
    date nvarchar(255) NOT NULL,
    rating nvarchar(255) NOT NULL,
    CONSTRAINT wine_rating_pk PRIMARY KEY (wine_id, user, date)
);
