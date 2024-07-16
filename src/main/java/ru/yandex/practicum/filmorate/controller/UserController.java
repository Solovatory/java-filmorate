package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private UserStorage userStorage;
    private final String friendPath = "/{id}/friends/{friend-id}";

    @Autowired
    public UserController(UserService userService, UserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userStorage.getUsers();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }


    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        return userStorage.updateUser(newUser);
    }

    @PutMapping(friendPath)
    public void addFriend(@PathVariable long id, @PathVariable("friend-id") long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(friendPath)
    public void removeFriend(@PathVariable long id, @PathVariable("friend-id") long friendId) {
        userService.removeFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> findFriends(@PathVariable long id) {
        return userService.getFriends(id);
    }

    @GetMapping("{id}/friends/common/{other-id}")
    public List<User> findMutualFriends(@PathVariable long id, @PathVariable("other-id") long otherId) {
        return userService.getMutualFriends(id, otherId);
    }
}
