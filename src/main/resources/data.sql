MERGE INTO FILMORATE_USER
(USER_ID, USER_NAME, USER_LOGIN, USER_EMAIL, USER_BIRTHDAY)
VALUES(1, 'Inna Murzina','InnaM', 'innam@test.com', '1975-02-21');

MERGE INTO FILMORATE_USER
(USER_ID, USER_NAME, USER_LOGIN, USER_EMAIL, USER_BIRTHDAY)
VALUES(2, 'Ivan Petrov','IvanP', 'ivanp@test.com', '1987-03-20');

MERGE INTO FILMORATE_USER
(USER_ID, USER_NAME, USER_LOGIN, USER_EMAIL, USER_BIRTHDAY)
VALUES(3, 'Svetlana Smirnova','SvetaS', 'svetas@test.com', '1991-11-03');

MERGE INTO FILMORATE_USER
(USER_ID, USER_NAME, USER_LOGIN, USER_EMAIL, USER_BIRTHDAY)
VALUES(4, 'Dmitry Komarov','DitryK', 'dmitryk@myhost.ru', '2000-07-30');

MERGE INTO FILMORATE_USER
(USER_ID, USER_NAME, USER_LOGIN, USER_EMAIL, USER_BIRTHDAY)
VALUES(5, 'John Smith','JohnS', 'johnS@gooogle.com', '1964-10-07');

MERGE INTO MPA_RATING (RATING_ID, RATING_DESC)
VALUES (1,'G'),
       (2,'PG'),
       (3,'PG-13'),
       (4,'R'),
       (5,'NC-17');

MERGE INTO GENRE
(GENRE_ID, GENRE_DESC)
VALUES (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальный'),
       (6, 'Боевик');

MERGE INTO FILM
(FILM_ID, FILM_NAME, FILM_DESCRIPTION, FILM_MPA_ID, RELEASE_DATE, FILM_DURATION)
VALUES (1, 'Властелин колец','Очень интересный фильм фантастический блокбастер по роману Толкиена',
        5, '1998-01-25', 90);

MERGE INTO FILM
(FILM_ID, FILM_NAME, FILM_DESCRIPTION, FILM_MPA_ID, RELEASE_DATE, FILM_DURATION)
VALUES (2, 'Ну Погоди','Мультфильм про Волка и Зайца',
        1, '1970-12-10', 15);

MERGE INTO FILM
(FILM_ID, FILM_NAME, FILM_DESCRIPTION, FILM_MPA_ID, RELEASE_DATE, FILM_DURATION)
VALUES (3, 'Красотка','Романтическая мелодрама с Джулией Робертс',
        3, '1995-12-30', 70);

MERGE INTO FILM
(FILM_ID, FILM_NAME, FILM_DESCRIPTION, FILM_MPA_ID, RELEASE_DATE, FILM_DURATION)
VALUES (4, 'Бриллиантовая рука','Советская комедия очень смешная',
        5, '1960-07-10', 60);

MERGE INTO FILM
(FILM_ID, FILM_NAME, FILM_DESCRIPTION, FILM_MPA_ID, RELEASE_DATE, FILM_DURATION)
VALUES (5, 'Фантомас','Легендарный французский приключенческий фильм',
        2, '1976-05-10', 90);

MERGE INTO FILM
(FILM_ID, FILM_NAME, FILM_DESCRIPTION, FILM_MPA_ID, RELEASE_DATE, FILM_DURATION)
VALUES (6, 'Паразиты','Социальная драма корейского режиссера, лауреат Оскара',
        4, '2021-06-09', 100);

MERGE INTO FRIENDS_STATUS (STATUS_ID, STATUS_DESC)
VALUES (1, 'confirmed'),
       (2, 'not confirmed');

