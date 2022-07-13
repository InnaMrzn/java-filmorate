package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component("userDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FriendshipStorage friendStorage;

    public UserDbStorage(JdbcTemplate jdbcTemplate, FriendshipStorage friendStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendStorage = friendStorage;
    }
    @Override
    public List<User> getUsers() {
        final String sql = "select USER_ID, USER_NAME, USER_LOGIN, USER_EMAIL, USER_BIRTHDAY" +
                " from USERS";
        List<User> users = jdbcTemplate.query(sql, (rs, rNum) -> mapUserFromRs(rs, rNum));
        log.debug("найдено " + users.size() + " пользователей.");
        return users;

    }
    private User mapUserFromRs(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("user_id");
        String name = rs.getString("user_name");
        String login = rs.getString("user_login");
        String email = rs.getString("user_email");
        LocalDate birthday = rs.getDate("user_birthday").toLocalDate();
        User result = new User(email,login,name,birthday);
        result.setId(id);

        return result;
    }
    @Override
    public User getUserById (long id) {
        final String sql = "select * from USERS where USER_ID= ?";
        List<User> users= jdbcTemplate.query(sql, (rs, rNum) -> mapUserFromRs(rs, rNum), id);
        if (users.size()!=1) {
            throw new NotFoundException(String.format("%s, Пользователь с ID= %s не найден",this.getClass(), id));
        }
        User user = users.get(0);
        log.debug("Успешно найден пользователь в id "+user.getId());
        return user;
    }
    @Override
    public long create(User user) {
        String sqlQuery = "insert into USERS (USER_NAME, USER_LOGIN, USER_EMAIL, USER_BIRTHDAY) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        log.debug("Успешно создан новый пользователь id "+user.getId());
        return keyHolder.getKey().longValue();
    }
    @Override
    public long update (User user) {
        String sqlQuery = "update USERS set USER_NAME = ?, USER_LOGIN = ?, USER_EMAIL = ?, " +
                "USER_BIRTHDAY = ? where USER_ID = ?";

        int rowCount = jdbcTemplate.update(sqlQuery, user.getName(), user.getLogin(),user.getEmail(),
                user.getBirthday(), user.getId());
        if (rowCount <1) {
            throw new NotFoundException(String.format("%s. Пользователь с ID= %s не найден",this.getClass(), user.getId()));
        }
        log.debug("Пользователь с id " + user.getId() + "успешно обновлен.");

        return user.getId();
    }
    @Override
    public boolean delete (long id){
        String sqlQuery = "delete from USERS where USER_ID = ?";
        int rowCount = jdbcTemplate.update(sqlQuery, id);
        if (rowCount <1) {
            throw new NotFoundException(String.format("%s. Пользователь с ID= %s не найден",this.getClass(), id));
        }
        log.debug("Пользователь с id " + id + "успешно удален.");
        return rowCount == 1;

    }
}
