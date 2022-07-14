package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmorateValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;


@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikesStorage likesStorage;
    private final GenreStorage genreStorage;


    public FilmService(FilmStorage filmStorage,
                       UserStorage userStorage, LikesStorage likesStorage,
                       GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likesStorage = likesStorage;
        this.genreStorage = genreStorage;
    }

    public Collection<Film> findAll() {

        List<Film> films = filmStorage.getFilms();
        for (Film film : films) {
            List<Genre> genres = genreStorage.findFilmGenres(film.getId());
            film.setGenres(genres);
        }
        return films;
    }

    public Film findById(long id) {
        Film film = filmStorage.getFilmById(id);
        film.setGenres(genreStorage.findFilmGenres(id));

        return film;
    }

    public Film create(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new FilmorateValidationException("Дата фильма не может быть раньше 28.12.1895");
        }
        long filmId = filmStorage.create(film);
        genreStorage.createFilmGenres(filmId, film.getGenres());
        return findById(filmId);
    }

    public Film update(Film film) {
        long filmId = filmStorage.update(film);
        genreStorage.deleteFilmGenres(filmId);
        genreStorage.createFilmGenres(filmId, film.getGenres());

        return findById(filmId);
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new NotFoundException("фильм с id=" + filmId + " Не найден");
        }
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("пользователь с id=" + userId + ", не найден");
        }
        likesStorage.createLike(userId, filmId);
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new NotFoundException("фильм с id=" + filmId + " Не найден");
        }
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("пользователь с id=" + userId + ", не найден");
        }
        likesStorage.deleteLike(userId, filmId);
    }

    public List<Film> getPopularFilms(Integer count) {
        if (count == null) {
            count = 10;
        }
        List<Film> films = filmStorage.getPopularFilms(count);

        for (Film film : films) {
            List<Genre> genres = genreStorage.findFilmGenres(film.getId());
            film.setGenres(genres);
        }
        return films;
    }

}
