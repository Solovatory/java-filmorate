package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class FilmRequest {
    Long id;
    @NotBlank(message = "Название фильма не может быть пустым")
    String name;
    @Size(max = 200, message = "Описание не может быть длиннее 200 символов")
    String description;
    LocalDate releaseDate;
    @Positive(message = "Продолжительность должна быть положительным числом")
    int duration;
    Integer likes;
    Set<Genre> genres;
    Mpa mpa;
}
