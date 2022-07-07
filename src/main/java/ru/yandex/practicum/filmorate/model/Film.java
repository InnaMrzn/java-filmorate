package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
public class Film {
    private long id;
    private Set<Long> likes = new HashSet<>();
    private Set<String> genres = new HashSet<>();
    private String rating;

    @NotBlank (message = "название фильма не должно быть пустым")
    private String name;

    @Size(max=200, message = "описание фильма должно быть не длиннее 200 символов")
    private String description;

    private LocalDate releaseDate;

    @Positive (message = "продолжительность фильма должна быть больше 0")
    private int duration;

    public Film (long id, String name, String description, LocalDate releaseDate,int duration) {
        this.id =id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film (String name, String description, LocalDate releaseDate,int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
