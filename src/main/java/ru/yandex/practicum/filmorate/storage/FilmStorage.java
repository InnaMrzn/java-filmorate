package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Set;

public interface FilmStorage {

    Film create (Film film);

    Film update (Film film);

    void delete (long id);

    Film getFilmById (long id);

    Set<Film> getFilms();


}
