package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class FilmDbStorageImpl implements FilmStorage {
    private final JdbcOperations jdbcOperations;
    private final GenreStorage genreStorage;

    public FilmDbStorageImpl(JdbcOperations jdbcOperations, GenreStorage genreStorage) {
        this.jdbcOperations = jdbcOperations;
        this.genreStorage = genreStorage;
    }

    @Override
    public List<Film> getAll() {
        final String sqlQuery = "select f.film_id, f.name, f.description, f.release_date, f.duration, " +
                "mpa.rating_id, mpa.rating_name from films as f join mpa on mpa.rating_id = f.rating_id";
        List<Film> films = jdbcOperations.query(sqlQuery, filmRowMapper());
        films.forEach(x -> x.setGenres(new LinkedHashSet<>(genreStorage.getAllFilmGenres(x.getId()))));
        return films;
    }

    @Override
    public Film getById(int id) {
        final String sqlQuery = "select f.film_id, f.name, f.description, f.release_date, f.duration, " +
                "mpa.rating_id, mpa.rating_name from films as f " +
                "join mpa on mpa.rating_id = f.rating_id " +
                "where film_id = ?";
        List<Film> films = jdbcOperations.query(sqlQuery, filmRowMapper(), id);
        if (films.size() != 1) {
            return null;
        }
        Film film = films.get(0);
        film.setGenres(new LinkedHashSet<>(genreStorage.getAllFilmGenres(film.getId())));
        return film;
    }

    @Override
    public Film add(Film film) {
        final String sqlQuery = "insert into films (name, description, release_date, duration, rating_id)" +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        updateGenres(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        final String sqlQuery = "update films " +
                "set name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? " +
                "where film_id = ?";
        jdbcOperations.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        updateGenres(film);
        return film;
    }

    @Override
    public void addLike(int filmId, int userId) {
        final String sqlQuery = "insert into likes (film_id, user_id) values (?, ?)";
        jdbcOperations.update(sqlQuery, filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        final String sqlQuery = "delete from likes where film_id = ? and user_id = ?";
        jdbcOperations.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> getTopFilms(int count) {
        String sqlQuery = "select f.film_id, f.name, f.description, f.release_date, f.duration, " +
                "mpa.rating_id, mpa.rating_name from films as f " +
                "join mpa on mpa.rating_id = f.rating_id " +
                "join likes as l on l.film_id = f.film_id " +
                "group by f.film_id " +
                "order by count(l.film_id) desc " +
                "limit ?";
        List<Film> films = jdbcOperations.query(sqlQuery, filmRowMapper(), count);
        films.forEach(x -> x.setGenres(new LinkedHashSet<>(genreStorage.getAllFilmGenres(x.getId()))));
        return films;
    }

    private void updateGenres(Film film) {
        final String sqlDelete = "delete from films_genres where film_id = ?";
        jdbcOperations.update(sqlDelete, film.getId());
        LinkedHashSet<Genre> genres = film.getGenres();
        if (genres != null) {
            final String sqlInsert = "insert into films_genres (film_id, genre_id) values (?, ?)";
            genres.forEach(x -> jdbcOperations.update(sqlInsert, film.getId(), x.getId()));
        }
    }

    private RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> new Film(
                rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new Mpa(rs.getInt("rating_id"), rs.getString("rating_name")));
    }
}