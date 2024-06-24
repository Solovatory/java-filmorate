package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Film {
    int id;
    @NotBlank(message = "Название фильма не может быть пустым")
    String name;
    @Size(max = 200, message = "Описание не может быть длиннее 200 символов")
    String description;
    LocalDate releaseDate;
    @Positive(message = "Продолжительность должна быть положительным числом")
    int duration;
}
