package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcOperations;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageImplTest {
    private final JdbcOperations jdbcOperations;
    private UserStorage userStorage;

    @BeforeEach
    public void beforeEach() {
        userStorage = new UserDbStorageImpl(jdbcOperations);
    }

    @Test
    public void testGetUserById() {
        User savedUser = userStorage.getById(1);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("ivan@mail.ru");
        assertThat(savedUser.getLogin()).isEqualTo("ivan111");
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = userStorage.getAll();

        assertThat(users.size()).isEqualTo(3);
    }

    @Test
    public void testAddNewUser() {
        User user = new User("email@email.com", "login", "name",
                LocalDate.of(2000, Month.JANUARY, 12));
        userStorage.add(user);

        assertThat(userStorage.getAll().size()).isEqualTo(4);
    }

    @Test
    public void testUpdateUser() {
        User savedUser = userStorage.getById(1);

        assertThat(savedUser.getEmail()).isEqualTo("ivan@mail.ru");

        User updatedUser = new User(1, "ivan_ivanov@email.com", "ivan3000", "Ivan Ivanov",
                LocalDate.of(2000, Month.JANUARY, 15));
        userStorage.update(updatedUser);

        savedUser = userStorage.getById(1);

        assertThat(savedUser.getEmail()).isEqualTo("ivan_ivanov@email.com");
    }

    @Test
    public void testAddAndDeleteFriend() {
        assertThat(userStorage.getAllFriends(1).size()).isEqualTo(0);

        userStorage.addFriend(1, 2);

        assertThat(userStorage.getAllFriends(1).size()).isEqualTo(1);

        userStorage.deleteFriend(1, 2);

        assertThat(userStorage.getAllFriends(1).size()).isEqualTo(0);
    }

    @Test
    public void testGetCommonFriends() {
        userStorage.addFriend(1, 2);
        userStorage.addFriend(1, 3);
        userStorage.addFriend(2, 3);
        assertThat(userStorage.getCommonFriends(1, 2)).isEqualTo(List.of(userStorage.getById(3)));
    }
}