package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {
    private Validator validator;
    private User user;

    @BeforeEach
    public void beforeEach() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        user = new User();
        user.setEmail("bob@yandex.ru");
        user.setLogin("bob");
        user.setBirthday(LocalDate.of(1992, Month.JANUARY, 15));
    }

    @Test
    public void createValidUser() {
        assertTrue(validator.validate(user).isEmpty());
    }

    @Test
    public void createUserWithBirthdayInFuture() {
        user.setBirthday(LocalDate.of(2030, Month.JANUARY, 15));
        assertEquals(1, validator.validate(user).size());
    }

    @Test
    public void createUserWithIncorrectEmail() {
        user.setEmail("yandex.ru");
        assertEquals(1, validator.validate(user).size());

        user.setEmail("");
        assertEquals(1, validator.validate(user).size());

        user.setEmail("@.rr");
        assertEquals(1, validator.validate(user).size());
    }

    @Test
    public void createUserWithEmptyLogin() {
        user.setLogin("");
        assertEquals(1, validator.validate(user).size());

        user.setLogin("    ");
        assertEquals(1, validator.validate(user).size());
    }
}