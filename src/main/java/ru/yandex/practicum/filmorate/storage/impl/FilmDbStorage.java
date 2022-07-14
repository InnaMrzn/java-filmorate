package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component("filmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, LikesStorage likesStorage) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getFilms() {
        final String sql = "select f.FILM_ID, f.FILM_NAME, f.FILM_DESCRIPTION, f.FILM_MPA_ID, f.RELEASE_DATE, f.FILM_DURATION, " +
                "m.RATING_DESC from FILMS as f left join MPA_RATINGS as m on f.FILM_MPA_ID = m.RATING_ID";
        List<Film> films = jdbcTemplate.query(sql, (rs, rNum) -> mapFilmFromRs(rs, rNum));
        log.debug(String.format("найдено %s фильмов", films.size()));
        return films;
    }

    @Override
    public List<Film> getPopularFilms(int limit) {
        String sqlQuery = "select f.film_id, f.film_name, f.FILM_DESCRIPTION, f.film_mpa_id, f.release_date," +
                "f.film_duration, m.RATING_DESC, count(likes.user_id) as likesNum " +
                "from FILMS as f left join MPA_RATINGS as m on f.FILM_MPA_ID = m.RATING_ID " +
                "left join FILM_LIKES as likes " +
                "on f.film_id = likes.FILM_ID " +
                "group by f.FILM_ID " +
                "order by likesNum desc " +
                "limit ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, (rs, rNum) -> mapFilmFromRs(rs, rNum), limit);
        log.debug(String.format("найдено %s популярных фильмов", films.size()));
        return films;
    }

    @Override
    public Film getFilmById(long id) {
        final String sql = "select f.FILM_ID, f.FILM_NAME, f.FILM_DESCRIPTION, f.FILM_MPA_ID, f.RELEASE_DATE, " +
                "f.FILM_DURATION, m.RATING_DESC from FILMS as f left join MPA_RATINGS as m on " +
                "f.FILM_MPA_ID = m.RATING_ID where f.FILM_ID = ?";

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rNum) -> mapFilmFromRs(rs, rNum), id);

        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException(String.format(" Фильм с ID= %s не найден", id));
        }
    }

    @Override
    public boolean delete(long id) {
        String sqlQuery = "delete from FILMS where FILM_ID = ?";
        int rowCount = jdbcTemplate.update(sqlQuery, id);
        log.debug("%s  Удален %s фильм.", this.getClass(), rowCount);
        return rowCount > 0;
    }

    @Override
    public long create(Film film) {
        String sqlQuery = "insert into FILMS (FILM_NAME, FILM_DESCRIPTION, FILM_MPA_ID, RELEASE_DATE, FILM_DURATION) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setInt(3, film.getMpa().getId());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(5, film.getDuration());
            return stmt;
        }, keyHolder);
        log.debug(String.format("%s. Успешно создан фильм с id %s", this.getClass(), keyHolder.getKey().longValue()));

        return keyHolder.getKey().longValue();

    }

    @Override
    public long update(Film film) {
        String sqlQuery = "update FILMS set FILM_NAME = ?, FILM_DESCRIPTION = ?, FILM_MPA_ID = ?, RELEASE_DATE = ?," +
                " FILM_DURATION =? where FILM_ID = ?";
        int rowCount = jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getMpa().getId(),
                film.getReleaseDate(), film.getDuration(), film.getId());
        if (rowCount < 1) {
            throw new NotFoundException(String.format(" Фильм с ID= %s не найден, невзможно обновить", film.getId()));
        }

        return film.getId();
    }

    private Film mapFilmFromRs(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("film_id");
        String name = rs.getString("film_name");
        String description = rs.getString("film_description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("film_duration");
        int mpaId = rs.getInt("film_mpa_id");
        String mpaDesc = rs.getString("rating_desc");
        Film result = new Film(name, description, releaseDate, duration);
        result.setMpa(new Mpa(mpaId, mpaDesc));
        result.setId(id);
        return result;
    }

}
