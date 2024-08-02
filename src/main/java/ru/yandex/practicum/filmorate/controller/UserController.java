package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.dto.UserRequest;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    private final String friendPath = "/{id}/friends/{friend-id}";
    private final String friendIdPath = "friend-id";

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserRequest request) {
        return userService.create(request);
    }

    @PutMapping
    public UserDto update(@Valid @RequestBody UserRequest request) {
        return userService.updateUser(request);
    }

    @PutMapping(friendPath)
    public void addFriend(@PathVariable long id, @PathVariable(friendIdPath) long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(friendPath)
    public void removeFriend(@PathVariable long id, @PathVariable(friendIdPath) long friendId) {
        userService.removeFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<UserDto> findFriends(@PathVariable long id) {
        return userService.getFriends(id);
    }

    @GetMapping("{id}/friends/common/{other-id}")
    public List<UserDto> findMutualFriends(@PathVariable long id, @PathVariable("other-id") long otherId) {
        return userService.getMutualFriends(id, otherId);
    }
}
