package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.util.List;

@Component("friendDbStorage")
@Slf4j
public class FriendshipDbStorage implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;
    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createFriendRequest(long requesterId, long approverId){
        String sqlQuery = "insert into FRIENDS (USER_ID, FRIEND_ID, STATUS_ID) " +
                "values (?, ?, 0)";
        jdbcTemplate.update(sqlQuery, requesterId, approverId);
        log.debug("Новый запрос в друзья успешно добавден в базу. Отправитель "
               +requesterId+ ", получатель "+ approverId);
    }

    @Override
    public void confirmFriendRequest (long requesterId, long approverId){
        String sqlQuery = "update FRIENDS SET STATUS_ID = 1 where USER_ID = ? and FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery,requesterId, approverId);
        log.debug("Новый запрос в друзья успешно подтвержден в базе. Отправитель "
                +requesterId+ ", одобрено пользователем "+ approverId);
    }

    @Override
    public List<Long> findFriendsByUser (long userId){
        String sql = "select friend_id from friends where user_id=?" +
                "union select user_id from friends where friend_id=? and STATUS_ID =1";
        List<Long> friends = jdbcTemplate.query(sql, (rs, rNum) -> rs.getLong("friend_id"), userId, userId);
        log.debug("Список друзей пользователя с id "+userId+" имеет "+friends.size());
        return friends;

    }

    @Override
    public void removeFriendship (long userId, long friendId) {
        String sqlQuery = "delete from FRIENDS where USER_ID = ? and FRIEND_ID = ?";
        int rowCount = jdbcTemplate.update(sqlQuery,userId, friendId);
        log.debug("удалена дружба между пользователями "
                +userId+ ", одобрено пользователем "+ friendId);
    }

    @Override
    public void removeFriendsForUser (long userId) {
        String sqlQuery = "delete from FRIENDS where USER_ID = ?";
        int rowCount = jdbcTemplate.update(sqlQuery,userId);
    }

}
