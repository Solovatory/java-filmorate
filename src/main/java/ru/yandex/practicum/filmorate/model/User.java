package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
int id;
@NotBlank(message = "Электронная почта не может быть пустой")
@Email(message = "Неверный адрес электронной почты")
String email;
@NotBlank(message = "Логин не может быть пустым")
String login;
String name;
@PastOrPresent(message = "Дата рождения не может быть в будущем")
LocalDate birthday;
}
