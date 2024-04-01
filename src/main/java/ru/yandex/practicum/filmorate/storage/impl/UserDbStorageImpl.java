package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class UserDbStorageImpl implements UserStorage {
    private final JdbcOperations jdbcOperations;

    public UserDbStorageImpl(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<User> getAll() {
        final String sqlQuery = "select * from users";
        return jdbcOperations.query(sqlQuery, userRowMapper());
    }

    @Override
    public User getById(int id) {
        final String sqlQuery = "select * from users where user_id = ?";
        List<User> users = jdbcOperations.query(sqlQuery, userRowMapper(), id);
        if (users.size() != 1) {
            return null;
        }
        return users.get(0);
    }

    @Override
    public User add(User user) {
        final String sqlQuery = "insert into users (email, login, name, birthday)" +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public User update(User user) {
        final String sqlQuery = "update users set email = ?, login = ?, name = ?, birthday = ? " +
                "where user_id = ?";
        jdbcOperations.update(sqlQuery, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void addFriend(int id, int friendId) {
        final String sqlQuery = "insert into friendships (user_id, friend_id) values (?, ?)";
        jdbcOperations.update(sqlQuery, id, friendId);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        final String sqlQuery = "delete from friendships where user_id = ? and friend_id = ?";
        jdbcOperations.update(sqlQuery, id, friendId);
    }

    @Override
    public List<User> getAllFriends(int id) {
        final String sqlQuery = "select u.* from users as u " +
                "join friendships as fs on fs.friend_id = u.user_id " +
                "where fs.user_id = ?";
        return jdbcOperations.query(sqlQuery, userRowMapper(), id);
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        final String sqlQuery = "select u.* from users as u " +
                "join friendships as f on f.friend_id  = u.user_id " +
                "where f.user_id = ? or f.user_id = ? " +
                "group by f.friend_id " +
                "having count(f.friend_id) > 1";
        return jdbcOperations.query(sqlQuery, userRowMapper(), id, otherId);
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate());
    }
}