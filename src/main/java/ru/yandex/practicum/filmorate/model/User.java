package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.*;

import javax.validation.constraints.*;

@Data
public class User {

    private long id;
    private Set<Long> friends = new HashSet<>();
    private Set<Long> likedFilms = new HashSet<>();

    @NotEmpty (message = "email не может быть пустым")
    @Email (message = "неправильный формат email")
    private final String email;

    @NotEmpty (message = "логин не может быть пустым")
    @Pattern(regexp="^\\S*$", message = "в логине не должно быть пробелов")
    private final String login;

    @NonNull
    private String name;

    @PastOrPresent (message ="дата рождения не может быть в будущем")
    private final LocalDate birthday;

}
