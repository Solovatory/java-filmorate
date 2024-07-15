package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final String wrongEmailMessage = "Электронная почта не может быть пустой и должна содержать символ @";
    private final String wrongLoginMessage = "Логин не может быть пустым и содержать пробелы";
    private final String wrongBirthdayMessage = "Дата рождения не может быть в будущем";

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User getUser(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        } else {
            return user;
        }
    }

    @Override
    public User addUser(User user) {
        log.trace("Начало создания пользователя");
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn(wrongEmailMessage);
            throw new ValidationException(wrongEmailMessage);
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn(wrongLoginMessage);
            throw new ValidationException(wrongLoginMessage);
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn(wrongBirthdayMessage);
            throw new ValidationException(wrongBirthdayMessage);
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.trace("Имя пользователя не указано");
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        log.trace("Добавление пользователя с уникальным идентификатором");
        users.put(user.getId(), user);
        log.trace("Новый пользователь успешно добавлен");
        return user;
    }

    @Override
    public User updateUser(User newUser) {
        log.trace("Начало обновления данных пользователя");
        User oldUser = users.get(newUser.getId());
        if (oldUser == null) {
            throw new NotFoundException("Такого пользователя не существует");
        } else {
            oldUser.setBirthday(newUser.getBirthday());
            oldUser.setName(newUser.getName());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            log.trace("Данные пользователя обновлены");
            return oldUser;
        }
    }

    private long getNextId() {
        log.trace("Получение уникального идентификатора для пользователя");
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
