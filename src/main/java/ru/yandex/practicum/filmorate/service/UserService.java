package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
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
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (user != null && friend != null) {
            userStorage.getUser(userId).getFriends().add(friendId);
            userStorage.getUser(friendId).getFriends().add(userId);
        } else {
            throw new NotFoundException(notFoundMessage);
        }
    }

    public void removeFriend(Long id, Long friendId) {
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);
        if (user != null && friend != null) {
            userStorage.getUser(id).getFriends().remove(friendId);
            userStorage.getUser(friendId).getFriends().remove(id);
        } else {
            throw new NotFoundException(notFoundMessage);
        }
    }

    public List<User> getFriends(long id) {
        User user = userStorage.getUser(id);
        if (user != null) {
            return userStorage.getUser(id).getFriends().stream().map(userStorage::getUser).toList();
        } else {
            throw new NotFoundException(notFoundMessage);
        }
    }

    public List<User> getMutualFriends(Long id, Long otherId) {
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(otherId);
        if (user != null && friend != null) {
            return user.getFriends()
                    .stream()
                    .filter(userId -> friend.getFriends().contains(userId))
                    .map(userStorage::getUser)
                    .toList();
        } else {
            throw new NotFoundException(notFoundMessage);
        }
    }
}
