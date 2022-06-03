package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import lombok.*;

import javax.validation.constraints.*;

@Data
public class User {

    private int id;

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
