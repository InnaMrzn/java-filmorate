package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Set;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage{

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Film> getFilms() {
        return null;
        /*new HashSet<Film> (
                films.values());*/
    }

    @Override
    public Film getFilmById(long id) {
       /* if (films.get(id) == null) {
            throw new NotFoundException(String.format("Фильм с id %s не найден", id));
        }
        return films.get(id);*/
        return null;
    }

    @Override
    public void delete (long id) {
        /*if (films.get(id) == null) {
            throw new NotFoundException(String.format("Невозможно удалить фильм. Фильм с id %s не найден", id));
        }
        films.remove(id);*/
    }

    @Override
    public Film create(Film film) {
        /*films.put(film.getId(),film);
        log.info("Новый фильм успешно добавлен с ID: '{}'", film.getId());
        return film;*/
        return null;
    }

    @Override
    public Film update(Film film) {
        /*if (films.get(film.getId()) == null) {
            throw new NotFoundException(String.format("Невозможно обновить. Фильм с id %s не найден",
                    film.getId()));
        }
        films.put(film.getId(),film);
        log.info("Фильм с ID '{}' успешно изменен",
                film.getId());
        return film;*/
        return null;
    }
}
