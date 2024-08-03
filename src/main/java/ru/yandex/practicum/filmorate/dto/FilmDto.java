package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class FilmDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long id;
    String name;
    String description;
    LocalDate releaseDate;
    int duration;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Integer likes;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Set<Genre> genres;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Mpa mpa;
}
