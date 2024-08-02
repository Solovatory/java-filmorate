package ru.yandex.practicum.filmorate.dal.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Collection<User> getUsers();

    Optional<User> getUser(Long id);

    User addUser(User user);

    User updateUser(User newUser);

    void addFriend(Long userId, Long friendId);

    void removeFriend(Long id, Long friendId);

    List<User> getFriends(Long id);

    List<User> getMutualFriends(Long id, Long friendId);
}
