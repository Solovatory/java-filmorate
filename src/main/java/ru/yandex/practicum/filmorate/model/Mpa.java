package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class Mpa {
    @Id
    int id;
    @NotBlank(message = "Рейтинг должен иметь значение")
    String name;
}
