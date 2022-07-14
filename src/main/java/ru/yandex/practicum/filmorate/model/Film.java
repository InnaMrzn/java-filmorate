package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.*;

import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class Film {
    private Long id;
    private Mpa mpa;
    private List<Genre> genres = new ArrayList<>();

    @NotBlank (message = "название фильма не должно быть пустым")
    private final String name;

    @Size(max=200, message = "описание фильма должно быть не длиннее 200 символов")
    private final String description;

    private final LocalDate releaseDate;

    @Positive (message = "продолжительность фильма должна быть больше 0")
    private final int duration;



}
