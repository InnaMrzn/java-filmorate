package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserStorage {

    User create (User user);

    User update (User user);

    void delete (long id );

    User getUserById (long id);

    Set<User> getUsers();

}
