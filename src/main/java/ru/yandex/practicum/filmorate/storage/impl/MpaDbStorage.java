package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("mpaDbStorage")
@Slf4j
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getMpas() {
        final String sql = "select RATING_ID, RATING_DESC from MPA_RATINGS";
        List<Mpa> mpas = jdbcTemplate.query(sql, (rs, rNum) -> mapMpaFromRs(rs, rNum));
        log.debug("найдено " + mpas.size() + " значений MPA рейтинга.");
        return mpas;
    }

    private Mpa mapMpaFromRs(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("rating_id");
        String desc = rs.getString("rating_desc");

        return new Mpa(id,desc);
    }

    @Override
    public Mpa getMpaById (int id) {
        final String sql = "select RATING_ID, RATING_DESC from MPA_RATINGS where RATING_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rNum) -> mapMpaFromRs(rs, rNum), id);

        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException(String.format(" MPA с ID= %s не найден",id));
        }


    }
}
