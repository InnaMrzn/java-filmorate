
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

INSERT INTO USERS
(USER_NAME, USER_LOGIN, USER_EMAIL, USER_BIRTHDAY)
VALUES('Inna Murzina','InnaM', 'innam@test.com', '1975-02-21');

INSERT INTO USERS
(USER_NAME, USER_LOGIN, USER_EMAIL, USER_BIRTHDAY)
VALUES('Ivan Petrov','IvanP', 'ivanp@test.com', '1987-03-20');

INSERT INTO FILMS
(FILM_NAME, FILM_DESCRIPTION, FILM_MPA_ID, RELEASE_DATE, FILM_DURATION)
VALUES ('Властелин колец','Очень интересный фильм фантастический блокбастер по роману Толкиена',
        5, '1998-01-25', 90);

INSERT INTO FILMS
(FILM_NAME, FILM_DESCRIPTION, FILM_MPA_ID, RELEASE_DATE, FILM_DURATION)
VALUES ('Ну Погоди','Мультфильм про Волка и Зайца',
        1, '1970-12-10', 15);

