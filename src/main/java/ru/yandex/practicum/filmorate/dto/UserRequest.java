package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class UserRequest {
    long id;
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Неверный адрес электронной почты")
    String email;
    @NotBlank(message = "Логин не может быть пустым")
    String login;
    String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    LocalDate birthday;

    public boolean hasLogin() {
        return login != null && !login.isEmpty();
    }

    public boolean hasName() {
        return name != null && !name.isEmpty();
    }

    public boolean hasEmail() {
        return email != null && !email.isEmpty();
    }

    public boolean hasBirthday() {
        return birthday != null;
    }
}
