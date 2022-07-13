package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    long create (Film film);

    long update (Film film);

    boolean delete (long id);

    Film getFilmById (long id);

    List<Film> getFilms();

    List<Film> getPopularFilms (int limit);


}
