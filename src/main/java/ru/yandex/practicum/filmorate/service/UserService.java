package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final String notFoundMessage = "Пользователь не найден";

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        return userStorage.addUser(user);
    }

    public void addFriend(Long userId, Long friendId) {
        log.trace("Добавление в друзья пользователей {} и {}", userId, friendId);
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriend(Long id, Long friendId) {
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
    }

    public List<User> getFriends(long id) {
        User user = userStorage.getUser(id);
        return user.getFriends().stream().map(userStorage::getUser).toList();
    }

    public List<User> getMutualFriends(Long id, Long otherId) {
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(otherId);
        return user.getFriends()
                .stream()
                .filter(userId -> friend.getFriends().contains(userId))
                .map(userStorage::getUser)
                .toList();

    }
}
