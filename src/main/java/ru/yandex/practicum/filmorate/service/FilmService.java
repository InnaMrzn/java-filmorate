package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmorateValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private int lastUsedId;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("inMemoryUserStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> findAll(){

        return filmStorage.getFilms();
    }

    public Film findById(long id){

        return filmStorage.getFilmById(id);
    }

    public Film create (Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new FilmorateValidationException("Дата фильма не может быть раньше 28.12.1895");
        }
        film.setId(getNextId());
        return filmStorage.create(film);
    }

    public Film update (Film film){
        return filmStorage.update(film);
    }

    public void addLike (Long filmId, Long userId){
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new NotFoundException("фильм с id=" + filmId + " Не найден");
        }
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("пользователь с id=" + userId + ", не найден");
        }
        film.getLikes().add(userId);
        user.getLikedFilms().add(filmId);
        filmStorage.update(film);
        userStorage.update(user);
    }
    public void deleteLike (Long filmId, Long userId){
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new NotFoundException("фильм с id=" + filmId + " Не найден");
        }
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("пользователь с id=" + userId + ", не найден");
        }
        film.getLikes().remove(userId);
        user.getLikedFilms().remove(filmId);
        filmStorage.update(film);
        userStorage.update(user);
    }

    public List<Film> getPopularFilms(Integer count){
        Collection<Film> films = filmStorage.getFilms();
        return films.stream().sorted((o1, o2) -> {
            int comp = o2.getLikes().size() -(o1.getLikes().size());
            return comp;
        }).limit(count).collect(Collectors.toList());
    }

    private int getNextId() {
        return ++lastUsedId;
    }
}
