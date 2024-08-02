package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Set;

@Data
public class FilmDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer likes;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Genre> genres;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Mpa mpa;
}
