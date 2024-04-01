package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User getById(int id) {
        User user = userStorage.getById(id);
        if (user == null) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        return user;
    }

    @Override
    public User add(User user) {
        if (userStorage.getAll().contains(user)) {
            throw new AlreadyExistsException("Пользователь уже существует");
        }
        return userStorage.add(user);
    }

    @Override
    public User update(User user) {
        if (userStorage.getById(user.getId()) == null) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        return userStorage.update(user);
    }

    @Override
    public void addFriend(int id, int friendId) {
        if (userStorage.getById(id) == null) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        if (userStorage.getById(friendId) == null) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        userStorage.addFriend(id, friendId);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        if (userStorage.getById(id) == null) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        if (userStorage.getById(friendId) == null) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        userStorage.deleteFriend(id, friendId);
    }

    @Override
    public List<User> getAllFriends(int id) {
        if (userStorage.getById(id) == null) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        return userStorage.getAllFriends(id);
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }
}