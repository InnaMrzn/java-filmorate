package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    public Set<Film> getFilms() {
        return new HashSet<Film>(films.values());
    }

    public Film getFilmById(long id) {
        if (films.get(id) == null) {
            throw new NotFoundException(String.format("Фильм с id %s не найден", id));
        }
        return films.get(id);
    }

    public void delete (long id) {
        if (films.get(id) == null) {
            throw new NotFoundException(String.format("Невозможно удалить фильм. Фильм с id %s не найден", id));
        }
        films.remove(id);
    }

    public Film create(Film film) {
        films.put(film.getId(),film);
        log.info("Новый фильм успешно добавлен с ID: '{}'", film.getId());
        return film;
    }

    public Film update(Film film) {
        if (films.get(film.getId()) == null) {
            throw new NotFoundException(String.format("Невозможно обновить. Фильм с id %s не найден",
                    film.getId()));
        }
        films.put(film.getId(),film);
        log.info("Фильм с ID '{}' успешно изменен",
                film.getId());
        return film;
    }
}
