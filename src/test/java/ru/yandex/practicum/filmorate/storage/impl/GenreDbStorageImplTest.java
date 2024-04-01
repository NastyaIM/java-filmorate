package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcOperations;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageImplTest {
    private final JdbcOperations jdbcOperations;
    private GenreStorage genreStorage;

    @BeforeEach
    public void beforeEach() {
        genreStorage = new GenreDbStorageImpl(jdbcOperations);
    }

    @Test
    public void testGetGenreById() {
        Genre savedGenre = genreStorage.getById(1);
        assertThat(savedGenre).isNotNull();
        assertThat(savedGenre.getName()).isEqualTo("Комедия");
    }

    @Test
    public void testGetAllGenres() {
        List<Genre> genres = genreStorage.getAll();
        assertThat(genres.size()).isEqualTo(6);
    }

    @Test
    public void testGetAllFilmGenres() {
        List<Genre> filmGenres = genreStorage.getAllFilmGenres(4);
        assertThat(filmGenres).isEqualTo(List.of(genreStorage.getById(1), genreStorage.getById(3)));
    }
}