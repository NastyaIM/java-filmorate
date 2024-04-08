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
    private static final String SQL_GET_ALL = "select * from users";
    private static final String SQL_GET_BY_ID = "select * from users where user_id = ?";
    private static final String SQL_ADD = "insert into users (email, login, name, birthday)" +
            "values (?, ?, ?, ?)";
    private static final String SQL_UPDATE = "update users set email = ?, login = ?, name = ?, birthday = ? " +
            "where user_id = ?";
    private static final String SQL_ADD_FRIEND = "insert into friendships (user_id, friend_id) values (?, ?)";
    private static final String SQL_DELETE_FRIEND = "delete from friendships where user_id = ? and friend_id = ?";
    private static final String SQL_GET_ALL_FRIENDS = "select u.* from users as u " +
            "join friendships as fs on fs.friend_id = u.user_id " +
            "where fs.user_id = ?";
    private static final String SQL_GET_COMMON_FRIENDS = "select u.* from users as u " +
            "join friendships as f on f.friend_id  = u.user_id " +
            "where f.user_id = ? or f.user_id = ? " +
            "group by f.friend_id " +
            "having count(f.friend_id) > 1";

    public UserDbStorageImpl(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<User> getAll() {
        return jdbcOperations.query(SQL_GET_ALL, userRowMapper());
    }

    @Override
    public User getById(int id) {
        List<User> users = jdbcOperations.query(SQL_GET_BY_ID, userRowMapper(), id);
        if (users.size() != 1) {
            return null;
        }
        return users.get(0);
    }

    @Override
    public User add(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_ADD, new String[]{"user_id"});
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
        jdbcOperations.update(SQL_UPDATE, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void addFriend(int id, int friendId) {
        jdbcOperations.update(SQL_ADD_FRIEND, id, friendId);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        jdbcOperations.update(SQL_DELETE_FRIEND, id, friendId);
    }

    @Override
    public List<User> getAllFriends(int id) {
        return jdbcOperations.query(SQL_GET_ALL_FRIENDS, userRowMapper(), id);
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        return jdbcOperations.query(SQL_GET_COMMON_FRIENDS, userRowMapper(), id, otherId);
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate());
    }
}