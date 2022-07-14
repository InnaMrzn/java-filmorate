package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

public interface UserStorage {

    long create (User user);

    long update (User user);

    boolean delete (long id );

    User getUserById (long id);

    List<User> getUsers();

}
