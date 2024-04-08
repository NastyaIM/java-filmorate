package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {
    private Validator validator;
    private Film film;

    @BeforeEach
    public void beforeEach() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        film = new Film();
        film.setName("film");
        film.setDescription("new film");
        film.setDuration(199);
        film.setReleaseDate(LocalDate.of(1999, Month.AUGUST, 14));
        film.setMpa(new Mpa(4));
    }

    @Test
    public void createValidFilm() {
        assertTrue(validator.validate(film).isEmpty());
    }

    @Test
    public void createFilmWithInvalidReleaseDate() {
        film.setReleaseDate(LocalDate.of(1111, Month.JANUARY, 1));
        assertEquals(1, validator.validate(film).size());
    }

    @Test
    public void createFilmWithNegativeDuration() {
        film.setDuration(-1000);
        assertEquals(1, validator.validate(film).size());
    }

    @Test
    public void createFilmWithEmptyName() {
        film.setName("");
        assertEquals(1, validator.validate(film).size());

        film.setName("    ");
        assertEquals(1, validator.validate(film).size());
    }

    @Test
    public void createFilmWithLongDescription() {
        film.setDescription("Very very very very very very very very very very very very very very very very" +
                " very very very very very very very very very very very very very very very " +
                "very very very very very very very very long description");
        assertTrue(film.getDescription().length() > 200);
        assertEquals(1, validator.validate(film).size());
    }
}