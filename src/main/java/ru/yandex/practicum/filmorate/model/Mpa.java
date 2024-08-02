package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Mpa {
    @Id
    private int id;
    @NotBlank(message = "Рейтинг должен иметь значение")
    private String name;
}
