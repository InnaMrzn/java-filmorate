package ru.yandex.practicum.filmorate.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmorateValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int lastUsedId;

    private int getNextId() {
        return ++lastUsedId;
    }

    @GetMapping
    public Set<User> findAll() {
        return new HashSet<User>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (user.getName().isBlank() || user.getName().isEmpty())
            user.setName(user.getLogin());
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Новый пользователь успешно добавлен с ID: '{}'",
                user.getId());
        return user;
    }

    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        if (user.getId()<=0) {
            throw new FilmorateValidationException("неверный ID пользователя для обновления "+user.getId());
        }
        users.put(user.getId(),user);
        log.info("Пользователь с ID '{}' успешно изменен", user.getId());
        return user;
    }
}
