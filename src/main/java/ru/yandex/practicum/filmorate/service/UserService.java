package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.user.UserStorage;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.dto.UserRequest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<UserDto> getUsers() {
        log.info("Получение списка всех пользователей");
        return userStorage.getUsers().stream().map(UserMapper::mapToUserDto).toList();
    }

    public UserDto getUserById(Long id) {
        log.trace("Получение пользователя с id = {}", id);
        User user = getUser(id);
        log.trace("В базе найден пользователь {} с id = {}", user, id);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto create(UserRequest request) {
        User user = UserMapper.mapToUser(request);
        log.trace("Добавление нового пользователя {}", user);
        return UserMapper.mapToUserDto(userStorage.addUser(user));
    }

    public UserDto updateUser(UserRequest request) {
        User oldUser = getUser(request.getId());
        User user = UserMapper.mapToUser(request);
        log.trace("Обновление данных пользователя {} на новые данные {}", oldUser, user);
        return UserMapper.mapToUserDto(userStorage.updateUser(user));
    }

    public void addFriend(long userId, long friendId) {
        log.trace("Добавление в друзья пользователей {} и {}", userId, friendId);
        if (userId == friendId) {
            log.warn("Попытка добавить пользователя себе в друзья");
            throw new ValidationException("Нельзя добавить в друзья себя");
        }
        User user = getUser(userId);
        User friend = getUser(friendId);
        userStorage.addFriend(userId, friendId);
        log.trace("Пользователь {} добавил в друзья {}", user, friend);
    }


    public void removeFriend(Long userId, Long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        log.trace("Пользователь {} удалил из друзей пользователя {}", user, friend);
        userStorage.removeFriend(userId, friendId);
    }

    public List<UserDto> getFriends(long id) {
        User user = getUser(id);
        log.info("Получение списка друзей для пользователя {}", user);
        return userStorage.getFriends(id).stream().map(UserMapper::mapToUserDto).toList();
    }

    public List<UserDto> getMutualFriends(Long id, Long otherId) {
        log.info("Получение списка общих друзей для пользователей с id {} и id {}", id, otherId);
        return userStorage.getMutualFriends(id, otherId).stream().map(UserMapper::mapToUserDto).toList();
    }

    private User getUser(Long id) {
        return userStorage.getUser(id).orElseThrow(()
                -> new NotFoundException(String.format("Пользователь с идентификатором = '%s' не найден", id)));
    }
}
