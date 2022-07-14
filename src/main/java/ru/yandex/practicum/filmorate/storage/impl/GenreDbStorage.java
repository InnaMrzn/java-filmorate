package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("genreDbStorage")
@Slf4j
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenres() {
        final String sql = "select GENRE_ID, GENRE_DESC from GENRES";
        List<Genre> genres = jdbcTemplate.query(sql, (rs, rNum) -> mapGenreFromRs(rs, rNum));
        log.debug("найдено " + genres.size() + " значений genre рейтинга.");
        return genres;

    }

    private Genre mapGenreFromRs(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("genre_id");
        String desc = rs.getString("genre_desc");

        return new Genre(id,desc);
    }

    @Override
    public Genre getGenreById (int id) {
        final String sql = "select GENRE_ID, GENRE_DESC from GENRES where GENRE_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rNum) -> mapGenreFromRs(rs, rNum), id);

        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException(String.format(" Жанр с ID= %s не найден",id));
        }

    }
    @Override
    public List<Genre> findFilmGenres (long filmId) {
        String sqlQuery = "select f.GENRE_ID, g.GENRE_DESC from FILM_GENRE as f LEFT JOIN GENRES as g " +
                "on f.GENRE_ID = g.GENRE_ID where f.FILM_ID = ?";

        return jdbcTemplate.query(sqlQuery, (rs, rNum) -> {
            return new Genre(rs.getInt("genre_id"), rs.getString("genre_desc"));
        }, filmId);
    }
    @Override
    public void createFilmGenres (long filmId, List<Genre> genres) {
        String sqlQuery = "MERGE INTO FILM_GENRE (GENRE_ID, FILM_ID) VALUES (?,?)";
        for (Genre genre: genres) {
            jdbcTemplate.update(sqlQuery, genre.getId(), filmId);
        }
    }

    @Override
    public boolean deleteFilmGenres (long filmId) {
        String sqlQuery = "delete from FILM_GENRE where film_id = ?";
        return jdbcTemplate.update(sqlQuery, filmId)>0;
    }
}
