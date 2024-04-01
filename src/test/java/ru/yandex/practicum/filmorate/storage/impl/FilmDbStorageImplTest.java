package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcOperations;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageImplTest {
    private final JdbcOperations jdbcOperations;
    private FilmStorage filmStorage;

    @BeforeEach
    public void beforeEach() {
        filmStorage = new FilmDbStorageImpl(jdbcOperations, new GenreDbStorageImpl(jdbcOperations));
    }

    @Test
    public void testGetFilmById() {
        Film savedFilm = filmStorage.getById(1);

        assertThat(savedFilm).isNotNull();
        assertThat(savedFilm.getName()).isEqualTo("film1");
        assertThat(savedFilm.getMpa().getName()).isEqualTo("G");
    }

    @Test
    public void testGetAllFilms() {
        List<Film> films = filmStorage.getAll();

        assertThat(films.size()).isEqualTo(4);
    }

    @Test
    public void testAddNewFilm() {
        Film newFilm = new Film("newFilm", "This is new Film",
                LocalDate.of(2023, Month.JANUARY, 11), 100, new Mpa(2));
        filmStorage.add(newFilm);

        assertThat(filmStorage.getAll().size()).isEqualTo(5);
    }

    @Test
    public void testUpdateFilm() {
        Film savedFilm = filmStorage.getById(1);

        assertThat(savedFilm.getMpa().getName()).isEqualTo("G");

        Film updatedFilm = new Film(1, "Film1", "Description1",
                LocalDate.of(2000, Month.JANUARY, 15), 100, new Mpa(4));
        filmStorage.update(updatedFilm);

        savedFilm = filmStorage.getById(1);

        assertThat(savedFilm.getMpa().getName()).isEqualTo("R");
    }

    @Test
    public void testGetTopFilms() {
        filmStorage.addLike(3, 2);
        filmStorage.addLike(3, 3);
        filmStorage.addLike(2, 3);
        List<Film> top3Films = filmStorage.getTopFilms(3);
        List<Film> top = List.of(filmStorage.getById(3), filmStorage.getById(2));
        assertThat(top3Films).isEqualTo(top);
    }

}