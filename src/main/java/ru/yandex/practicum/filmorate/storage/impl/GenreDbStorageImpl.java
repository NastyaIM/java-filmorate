package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class GenreDbStorageImpl implements GenreStorage {
    private final JdbcOperations jdbcOperations;
    private static final String SQL_GET_ALL = "select * from genres";
    private static final String SQL_GET_BY_ID = "select * from genres where genre_id = ?";
    private static final String SQL_GET_ALL_FILMS_GENRES = "select * from genres as g " +
            "join films_genres as fg on fg.genre_id = g.genre_id " +
            "join films as f on fg.film_id = f.film_id " +
            "where f.film_id = ? " +
            "order by fg.genre_id";

    public GenreDbStorageImpl(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<Genre> getAll() {
        return jdbcOperations.query(SQL_GET_ALL, genreRowMapper());
    }

    @Override
    public Genre getById(int id) {
        List<Genre> genres = jdbcOperations.query(SQL_GET_BY_ID, genreRowMapper(), id);
        if (genres.size() != 1) {
            return null;
        }
        return genres.get(0);
    }

    @Override
    public Set<Genre> getByIds(List<Integer> ids) {
        Set<Genre> genres = new LinkedHashSet<>();
        ids.forEach(id -> {
            Genre genre = getById(id);
            if (genre != null) {
                genres.add(genre);
            }
        });
        return genres;
    }

    @Override
    public Set<Genre> getAllFilmGenres(int filmId) {
        return new LinkedHashSet<>(jdbcOperations.query(SQL_GET_ALL_FILMS_GENRES, genreRowMapper(), filmId));
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> new Genre(
                rs.getInt("genre_id"),
                rs.getString("name"));
    }
}