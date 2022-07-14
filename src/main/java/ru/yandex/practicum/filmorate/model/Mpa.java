package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Mpa {

    private final Integer id;
    private final String name;
}
