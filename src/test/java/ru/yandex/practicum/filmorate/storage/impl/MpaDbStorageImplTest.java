package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcOperations;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageImplTest {
    private final JdbcOperations jdbcOperations;
    private MpaStorage mpaStorage;

    @BeforeEach
    public void beforeEach() {
        mpaStorage = new MpaDbStorageImpl(jdbcOperations);
    }

    @Test
    public void testGetMpaById() {
        Mpa savedMpa = mpaStorage.getById(3);
        assertThat(savedMpa).isNotNull();
        assertThat(savedMpa.getName()).isEqualTo("PG-13");
    }

    @Test
    public void testGetAllGenres() {
        List<Mpa> ratings = mpaStorage.getAll();
        assertThat(ratings.size()).isEqualTo(5);
    }
}