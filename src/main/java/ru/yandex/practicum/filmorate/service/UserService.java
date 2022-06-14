package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private int lastUsedId;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }
    public Set<User> findAll() {
        return userStorage.getUsers();
    }

    public User findById(long id) {
        return userStorage.getUserById(id);
    }

    public User create (User user){
        if (user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        return userStorage.create(user);
    }

    public User update (User user) {
        return userStorage.update(user);
    }

    public boolean addFriend (Long userId, Long friendId){
        User user = userStorage.getUserById(userId);
        if (user == null)
            throw new NotFoundException("пользователь с id="+userId+" Не найден");
        User friend = userStorage.getUserById(friendId);
        if (friend == null)
            throw new NotFoundException("пользователь с id="+friendId+", оправивший запрос в друзья, не найден");
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        userStorage.update(user);
        userStorage.update(friend);
        return true;
    }

    public boolean deleteFriend (Long userId, Long friendId){
        User user = userStorage.getUserById(userId);
        if (user == null)
            throw new NotFoundException("пользователь с id="+userId+" Не найден");
        User friend = userStorage.getUserById(friendId);
        if (friend == null)
            throw new NotFoundException("пользователь с id="+userId+", " +
                    "пославший запрос на удаление из друзей, не найден");
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        userStorage.update(user);
        userStorage.update(friend);
        return true;
    }
    public List<User> getFriends (Long userId){
        List friends = new ArrayList();
        User user = userStorage.getUserById(userId);
        if (user == null)
            throw new NotFoundException("пользователь с id="+userId+" Не найден");
        return user.getFriends().stream().map(friendId -> userStorage.getUserById(friendId))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends (Long userId, Long otherUserId){
        List<User> friends = new ArrayList();
        User user = userStorage.getUserById(userId);
        if (user == null)
            throw new NotFoundException("пользователь с id="+userId+" Не найден");
        User otherUser = userStorage.getUserById(otherUserId);
        if (otherUser == null)
            throw new NotFoundException("пользователь с id="+otherUserId+" Не найден");
        Set<Long> firstFriends = new HashSet(user.getFriends());
        firstFriends.retainAll(otherUser.getFriends());
        return firstFriends.stream().map(friendId -> userStorage.getUserById(friendId))
                .collect(Collectors.toList());
    }

    private int getNextId() {
        return ++lastUsedId;
    }
}
