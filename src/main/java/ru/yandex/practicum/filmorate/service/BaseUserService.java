package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class BaseUserService implements UserService {
    private final UserStorage storage;

    @Autowired
    public BaseUserService(UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<User> getAll() {
        return storage.getAll();
    }

    @Override
    public User getById(int id) {
        User user = storage.getById(id);
        if (user == null) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        return user;
    }

    @Override
    public User add(User user) {
        if (storage.getAll().contains(user)) {
            throw new AlreadyExistsException("Пользователь уже существует");
        }
        return storage.add(user);
    }

    @Override
    public User update(User user) {
        if (storage.getById(user.getId()) == null) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        return storage.update(user);
    }

    @Override
    public void addFriend(int id, int friendId) {
        if (storage.getById(id) == null) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        if (storage.getById(friendId) == null) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        storage.addFriend(id, friendId);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        if (storage.getById(id) == null) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        if (storage.getById(friendId) == null) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        storage.deleteFriend(id, friendId);
    }

    @Override
    public List<User> getAllFriends(int id) {
        if (storage.getById(id) == null) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        return storage.getAllFriends(id);
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        return storage.getCommonFriends(id, otherId);
    }
}
