
MERGE INTO MPA_RATINGS (RATING_ID, RATING_DESC)
VALUES (1,'G'),
       (2,'PG'),
       (3,'PG-13'),
       (4,'R'),
       (5,'NC-17');

MERGE INTO GENRES
(GENRE_ID, GENRE_DESC)
VALUES (1,'Комедия'),
       (2,'Драма'),
       (3,'Мультфильм'),
       (4,'Триллер'),
       (5,'Документальный'),
       (6,'Боевик');



MERGE INTO FRIENDS_STATUS (STATUS_ID, STATUS_DESC)
VALUES (1, 'confirmed'),
       (0, 'not confirmed');

