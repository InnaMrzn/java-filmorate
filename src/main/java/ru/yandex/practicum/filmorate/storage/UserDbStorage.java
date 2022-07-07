package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Component("userDbStorage")
public class UserDbStorage implements UserStorage{

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Collection<User> getUsers() {
        final String sql = "select * from FILMORATE_USER";
        Collection result = jdbcTemplate.query(sql, (rs, rNum) -> mapUserFromRs(rs, rNum));
        System.out.println("*****resut size="+result.size());
        return result;

    }

    private User mapUserFromRs(ResultSet rs, int rowNum) throws SQLException {
        System.out.println("Next RS");
        Long id = rs.getLong("user_id");
        String name = rs.getString("user_name");
        String login = rs.getString("user_login");
        String email = rs.getString("user_email");
        LocalDate birthday = rs.getDate("user_birthday").toLocalDate();

        return new User(id, name, login, email, birthday);
    }

    @Override
    public User getUserById (long id) {
        final String sql = "select * from FILMORATE_USER where USER_ID= ?";
        List<User> users= jdbcTemplate.query(sql, (rs, rNum) -> mapUserFromRs(rs, rNum), id);
        if (users.size()!=1) {
            throw new NotFoundException(String.format("Пользователь с ID= %s не найден",id));
        }
        return users.get(0);
    }

    @Override
    public User create(User user) {
        String sqlQuery = "insert into FILMORATE_USER(USER_NAME, USER_LOGIN, USER_EMAIL, USER_BIRTHDAY) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());

        return user;

    }

    @Override
    public User update(User user) {
        /*if (users.get(user.getId()) == null) {
            throw new NotFoundException(String.format("Невозможно обновить. Пользователь с id %s не найден",
                    user.getId()));
        }
        users.put(user.getId(),user);
        log.info("Пользователь с ID '{}' успешно изменен", user.getId());
        return user;*/
        return null;
    }

    @Override
    public void delete (long id){
        /*if (users.get(id) == null) {
            throw new NotFoundException(String.format("Невозможно удалить. Пользователь с id %s не найден", id));
        }
        users.remove(id);*/

    }
}
