package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.ArrayList;
import java.util.List;

@Component
public class GenreDbStorageImpl implements GenreStorage {
    private final JdbcOperations jdbcOperations;

    public GenreDbStorageImpl(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "select * from genres";
        return jdbcOperations.query(sqlQuery, genreRowMapper());
    }

    @Override
    public Genre getById(int id) {
        String sqlQuery = "select * from genres where genre_id = ?";
        List<Genre> genres = jdbcOperations.query(sqlQuery, genreRowMapper(), id);
        if (genres.size() != 1) {
            return null;
        }
        return genres.get(0);
    }

    @Override
    public List<Genre> getByIds(List<Integer> ids) {
        List<Genre> genres = new ArrayList<>();
        ids.forEach(id -> {
            Genre genre = getById(id);
            if (genre != null) {
                genres.add(genre);
            }
        });
        return genres;
    }

    @Override
    public List<Genre> getAllFilmGenres(int filmId) {
        String sqlQuery = "select * from genres as g " +
                "join films_genres as fg on fg.genre_id = g.genre_id " +
                "join films as f on fg.film_id = f.film_id " +
                "where f.film_id = ?";
        return jdbcOperations.query(sqlQuery, genreRowMapper(), filmId);
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> new Genre(
                rs.getInt("genre_id"),
                rs.getString("name"));
    }
}
