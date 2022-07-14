package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;
import java.util.List;

public interface GenreStorage {

    Genre getGenreById (int id);

    List<Genre> getGenres();
    List<Genre> findFilmGenres (long filmId);
    void createFilmGenres (long filmId, List<Genre> genres);
    boolean deleteFilmGenres (long filmId);
}
