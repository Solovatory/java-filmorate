package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@EqualsAndHashCode(of = {"id"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Неверный адрес электронной почты")
    String email;
    @NotBlank(message = "Логин не может быть пустым")
    String login;
    String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    LocalDate birthday;
}
