package ru.yandex.practicum.filmorate.dal.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, RowMapper<User> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Collection<User> getUsers() {
        String sql = "SELECT * FROM users";
        return findMany(sql);
    }

    @Override
    public Optional<User> getUser(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return findOne(sql, id);
    }

    @Override
    public User addUser(User user) {
        String sql = "INSERT INTO users(login, name, email, birthday) VALUES (?, ?, ?, ?)";
        long id = insert(sql, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday());
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User newUser) {
        String sql = "UPDATE users SET login = ?, name = ?, email = ?, birthday = ? WHERE id = ?";
        update(sql, newUser.getLogin(), newUser.getName(), newUser.getEmail(), newUser.getBirthday(), newUser.getId());
        return newUser;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sql = "INSERT INTO friendships (user_id, friend_id) " +
                "SELECT u1.id, u2.id " +
                "FROM users u1, users u2 " +
                "WHERE u1.id = ? AND u2.id = ? " +
                "AND NOT EXISTS ( " +
                "SELECT 1 " +
                "FROM friendships f " +
                "WHERE f.user_id = u1.id AND f.friend_id = u2.id)";
        insertNoKey(sql, userId, friendId);
    }

    @Override
    public void removeFriend(Long id, Long friendId) {
        String sql = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
        delete(sql, id, friendId);
    }

    @Override
    public List<User> getFriends(Long id) {
        String sql = "SELECT u.* FROM users u JOIN friendships f ON u.id = f.friend_id WHERE f.user_id = ?";
        return findMany(sql, id);
    }

    @Override
    public List<User> getMutualFriends(Long id, Long friendId) {
        String sql = "SELECT u.* " +
                "FROM friendships f1 " +
                "JOIN friendships f2 ON f1.friend_id = f2.friend_id " +
                "JOIN users u ON u.id = f1.friend_id " +
                "WHERE f1.user_id = ?" +
                "AND f2.user_id = ?";
        return findMany(sql, id, friendId);
    }
}
