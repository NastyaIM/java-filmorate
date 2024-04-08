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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@Slf4j
public class FilmDbStorageImpl implements FilmStorage {
    private final JdbcOperations jdbcOperations;
    private final GenreStorage genreStorage;
    private static final String SQL_GET_ALL = "select * from films as f " +
            "join mpa on mpa.rating_id = f.rating_id";
    private static final String SQL_GET_BY_ID = "select * from films as f " +
            "join mpa on mpa.rating_id = f.rating_id " +
            "where film_id = ?";
    private static final String SQL_ADD = "insert into films (name, description, release_date, duration, rating_id)" +
            "values (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "update films " +
            "set name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? " +
            "where film_id = ?";
    private static final String SQL_ADD_LIKE = "insert into likes (film_id, user_id) values (?, ?)";
    private static final String SQL_DELETE_LIKE = "delete from likes where film_id = ? and user_id = ?";
    private static final String SQL_GET_TOP_FILMS = "select f.*, mpa.* from films as f " +
            "join mpa on mpa.rating_id = f.rating_id " +
            "join likes as l on l.film_id = f.film_id " +
            "group by f.film_id " +
            "order by count(l.film_id) desc " +
            "limit ?";
    private static final String SQL_ADD_GENRES = "insert into films_genres (film_id, genre_id) values (?, ?)";
    private static final String SQL_DELETE_GENRES = "delete from films_genres where film_id = ?";

    public FilmDbStorageImpl(JdbcOperations jdbcOperations, GenreStorage genreStorage) {
        this.jdbcOperations = jdbcOperations;
        this.genreStorage = genreStorage;
    }

    @Override
    public List<Film> getAll() {
        List<Film> films = jdbcOperations.query(SQL_GET_ALL, filmRowMapper());
        films.forEach(x -> x.setGenres(genreStorage.getAllFilmGenres(x.getId())));
        return films;
    }

    @Override
    public Film getById(int id) {
        List<Film> films = jdbcOperations.query(SQL_GET_BY_ID, filmRowMapper(), id);
        if (films.size() != 1) {
            return null;
        }
        Film film = films.get(0);
        film.setGenres(genreStorage.getAllFilmGenres(film.getId()));
        return film;
    }

    @Override
    public Film add(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_ADD, new String[]{"film_id"});
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
        jdbcOperations.update(SQL_UPDATE, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        updateGenres(film);
        return film;
    }

    @Override
    public void addLike(int filmId, int userId) {
        jdbcOperations.update(SQL_ADD_LIKE, filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        jdbcOperations.update(SQL_DELETE_LIKE, filmId, userId);
    }

    @Override
    public List<Film> getTopFilms(int count) {
        List<Film> films = jdbcOperations.query(SQL_GET_TOP_FILMS, filmRowMapper(), count);
        films.forEach(x -> x.setGenres(new HashSet<>(genreStorage.getAllFilmGenres(x.getId()))));
        return films;
    }

    private void updateGenres(Film film) {
        jdbcOperations.update(SQL_DELETE_GENRES, film.getId());
        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            genres.forEach(x -> jdbcOperations.update(SQL_ADD_GENRES, film.getId(), x.getId()));
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