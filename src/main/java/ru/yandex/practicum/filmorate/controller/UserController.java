package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() {
        log.debug("Всего пользователей: " + users.size());
        return new ResponseEntity<>(new ArrayList<>(users.values()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) throws ValidationException {
        if (users.containsValue(user)) {
            log.warn("Пользователь с таким email уже существует");
            throw new ValidationException("Пользователь уже существует");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        int userId = getId();
        user.setId(userId);
        log.debug("Добавляем пользователя {} с id {}", user, userId);
        users.put(user.getId(), user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            log.warn("Нельзя обновить данные, неправильный id");
            throw new ValidationException("Пользователя с таким идентификатором нет");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        log.debug("Будет обновлен пользователь с id: {} на {}", user.getId(), user);
        users.put(user.getId(), user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private int getId() {
        return id++;
    }
}