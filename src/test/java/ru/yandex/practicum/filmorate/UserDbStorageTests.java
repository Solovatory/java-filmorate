package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dal.user.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class UserDbStorageTests {
    private final UserDbStorage userStorage;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("sss@mail.ru");
        testUser.setLogin("Login");
        testUser.setName("Test User");
        testUser.setBirthday(LocalDate.of(1997, 4, 12));
    }

    @Test
    public void testAddUser() {
        User addedUser = userStorage.addUser(testUser);
        assertThat(addedUser.getId()).isNotNull();
        assertThat(addedUser.getEmail()).isEqualTo("sss@mail.ru");
        assertThat(addedUser.getLogin()).isEqualTo("Login");
    }

    @Test
    public void testFindUserById() {
        User addedUser = userStorage.addUser(testUser);
        Optional<User> userOptional = userStorage.getUser(addedUser.getId());

        assertThat(userOptional).isPresent().hasValueSatisfying(user -> {
            assertThat(user).hasFieldOrPropertyWithValue("id", addedUser.getId());
            assertThat(user).hasFieldOrPropertyWithValue("email", "sss@mail.ru");
            assertThat(user).hasFieldOrPropertyWithValue("login", "Login");
        });
    }

    @Test
    public void testUpdateUser() {
        User addedUser = userStorage.addUser(testUser);
        addedUser.setEmail("newemail@mail.ru");
        addedUser.setLogin("NewLogin");
        User updatedUser = userStorage.updateUser(addedUser);

        Optional<User> userOptional = userStorage.getUser(updatedUser.getId());

        assertThat(userOptional).isPresent().hasValueSatisfying(user -> {
            assertThat(user).hasFieldOrPropertyWithValue("id", updatedUser.getId());
            assertThat(user).hasFieldOrPropertyWithValue("email", "newemail@mail.ru");
            assertThat(user).hasFieldOrPropertyWithValue("login", "NewLogin");
        });
    }

    @Test
    public void testGetUsers() {
        userStorage.addUser(testUser);
        Collection<User> users = userStorage.getUsers();
        assertThat(users).asList().isNotEmpty();
    }

    @Test
    public void testAddAndRemoveFriend() {
        User user1 = userStorage.addUser(testUser);
        User user2 = new User();
        user2.setEmail("friend@mail.ru");
        user2.setLogin("FriendLogin");
        user2.setName("Friend User");
        user2.setBirthday(LocalDate.of(1990, 1, 1));
        User friendUser = userStorage.addUser(user2);

        userStorage.addFriend(user1.getId(), friendUser.getId());
        List<User> friends = userStorage.getFriends(user1.getId());
        Assertions.assertFalse(friends.isEmpty());

        userStorage.removeFriend(user1.getId(), friendUser.getId());
        friends = userStorage.getFriends(user1.getId());
        Assertions.assertTrue(friends.isEmpty());
    }

    @Test
    public void testGetMutualFriends() {
        User user1 = userStorage.addUser(testUser);
        User user2 = new User();
        user2.setEmail("friend1@mail.ru");
        user2.setLogin("FriendLogin1");
        user2.setName("Friend User 1");
        user2.setBirthday(LocalDate.of(1990, 1, 1));
        User friendUser1 = userStorage.addUser(user2);

        User user3 = new User();
        user3.setEmail("friend2@mail.ru");
        user3.setLogin("FriendLogin2");
        user3.setName("Friend User 2");
        user3.setBirthday(LocalDate.of(1991, 2, 2));
        User friendUser2 = userStorage.addUser(user3);

        userStorage.addFriend(user1.getId(), friendUser1.getId());
        userStorage.addFriend(user1.getId(), friendUser2.getId());
        userStorage.addFriend(friendUser1.getId(), friendUser2.getId());

        List<User> mutualFriends = userStorage.getMutualFriends(user1.getId(), friendUser1.getId());
        assertThat(mutualFriends).isNotNull();
        Assertions.assertEquals("Friend User 2", mutualFriends.getFirst().getName());
    }
}