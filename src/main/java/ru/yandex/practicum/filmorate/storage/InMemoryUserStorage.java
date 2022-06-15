package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public Set<User> getUsers() {
        return new HashSet<User>(users.values());
    }

    public User getUserById (long id) {
        if (users.get(id) == null) {
            throw new NotFoundException(String.format("Пользователь с id %s не найден", id));
        }
        return users.get(id);
    }

    public User create(User user) {
        users.put(user.getId(), user);
        log.info("Новый пользователь успешно добавлен с ID: '{}'", user.getId());
        return user;
    }

    public User update(User user) {
        if (users.get(user.getId()) == null) {
            throw new NotFoundException(String.format("Невозможно обновить. Пользователь с id %s не найден",
                    user.getId()));
        }
        users.put(user.getId(),user);
        log.info("Пользователь с ID '{}' успешно изменен", user.getId());
        return user;
    }

    public void delete (long id){
        if (users.get(id) == null) {
            throw new NotFoundException(String.format("Невозможно удалить. Пользователь с id %s не найден", id));
        }
        users.remove(id);
    }

}
