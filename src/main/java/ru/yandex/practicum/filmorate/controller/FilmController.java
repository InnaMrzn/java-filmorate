package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import ru.yandex.practicum.filmorate.exception.FilmorateValidationException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int lastUsedId;

    private int getNextId() {
        return ++lastUsedId;
    }

    @GetMapping
    public Set<Film> findAll() {
        return new HashSet<Film>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new FilmorateValidationException("Дата фильма не может быть раньше 28.12.1895");
        }
        film.setId(getNextId());
        films.put(film.getId(),film);
        log.info("Новый фильм успешно добавлен с ID: '{}'",
                film.getId());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        if (film.getId()<=0) {
            throw new FilmorateValidationException("неверный ID фильма для обновления "+film.getId());
        }
        films.put(film.getId(),film);
        log.info("Фильм с ID '{}' успешно изменен",
                film.getId());
        return film;
    }
}