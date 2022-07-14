package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipStorage friendsStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendshipStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = friendStorage;
    }
    public Collection<User> findAll() {
        List<User> users = userStorage.getUsers();
        for (User user: users) {
            user.setFriends(friendsStorage.findFriendsByUser(user.getId()));
        }
        return users;
    }

    public User findById(long id) {
        User user = userStorage.getUserById(id);
        user.setFriends(friendsStorage.findFriendsByUser(user.getId()));
        return user;
    }

    public User create (User user){
        if (user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        long userId = userStorage.create(user);

        return findById(userId);
    }

    public User update (User user) {
        long userId = userStorage.update(user);

        return findById(user.getId());
    }

    public void addFriend (Long userId, Long friendId){
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("пользователь с id=" + userId + " Не найден");
        }
        User friend = userStorage.getUserById(friendId);
        if (friend == null) {
            throw new NotFoundException("пользователь с id=" + friendId + ", оправивший запрос в друзья, не найден");
        }
        friendsStorage.createFriendRequest(userId, friendId);

    }

    public void deleteFriend (Long userId, Long friendId){
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("пользователь с id=" + userId + " Не найден");
        }
        User friend = userStorage.getUserById(friendId);
        if (friend == null) {
            throw new NotFoundException("пользователь с id=" + userId + ", " +
                    "пославший запрос на удаление из друзей, не найден");
        }
        friendsStorage.removeFriendship(userId, friendId);
    }

    public List<User> getFriends (Long userId){
        User user = findById(userId);
        if (user == null) {
            throw new NotFoundException("пользователь с id=" + userId + " Не найден");
        }

        return user.getFriends().stream().map(friendId -> userStorage.getUserById(friendId))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends (Long userId, Long otherUserId){
        User user = findById(userId);
        if (user == null) {
            throw new NotFoundException("пользователь с id=" + userId + " Не найден");
        }
        User otherUser = findById(otherUserId);
        if (otherUser == null) {
            throw new NotFoundException("пользователь с id=" + otherUserId + " Не найден");
        }
        Set<Long> firstFriends = new HashSet<>(user.getFriends());
        firstFriends.retainAll(otherUser.getFriends());
        return firstFriends.stream().map(friendId -> userStorage.getUserById(friendId))
                .collect(Collectors.toList());
    }


}
