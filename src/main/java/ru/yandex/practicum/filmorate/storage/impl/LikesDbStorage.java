package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import java.util.List;

@Component("likesDbStorage")
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikesDbStorage(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public void createLike (long userId, long filmId){
        String sqlQuery = "MERGE into FILM_LIKES (USER_ID, FILM_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }
    @Override
    public void deleteLike (long userId, long filmId){
        String sqlQuery = "delete from FILM_LIKES where user_id = ? and film_id = ?";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }
    @Override
    public List<Long> findFilmLikesByUser(long userId){
        final String sql = "select FILM_ID from FILM_LIKES where USER_ID= ?";
        return jdbcTemplate.query(sql, (rs, rNum) -> rs.getLong("film_id"), userId);
    }
    @Override
    public List<Long> findLikedUsersByFilm(long filmId){
        final String sql = "select USER_ID from FILM_LIKES where FILM_ID= ?";
        return jdbcTemplate.query(sql, (rs, rNum) -> rs.getLong("user_id"), filmId);
    }

}
