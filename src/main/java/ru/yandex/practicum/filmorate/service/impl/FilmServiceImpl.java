package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage, UserStorage userStorage, MpaStorage mpaStorage, GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film getById(int id) {
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new NotFoundException("Фильма с таким id не существует");
        }
        film.setGenres(genreStorage.getAllFilmGenres(id));
        return film;
    }

    @Override
    public Film add(Film film) {
        if (filmStorage.getAll().contains(film)) {
            throw new AlreadyExistsException("Фильм уже существует");
        }
        if (mpaStorage.getById(film.getMpa().getId()) == null) {
            throw new ValidationException("Неправильный рейтинг");
        }
        if (film.getGenres() != null) {
            List<Integer> ids = film.getGenres().stream()
                    .map(Genre::getId).collect(Collectors.toList());
            Set<Genre> genres = genreStorage.getByIds(ids);
            if (ids.size() != genres.size()) {
                throw new ValidationException("Неправильный жанр");
            }
            film.setGenres(genres);
        }
        return filmStorage.add(film);
    }

    @Override
    public Film update(Film film) {
        if (filmStorage.getById(film.getId()) == null) {
            throw new NotFoundException("Фильма с таким id не существует");
        }
        if (mpaStorage.getById(film.getMpa().getId()) == null) {
            throw new ValidationException("Неправильный рейтинг");
        }
        return filmStorage.update(film);
    }

    @Override
    public void addLike(int id, int userId) {
        if (filmStorage.getById(id) == null) {
            throw new NotFoundException("Фильма с таким id не существует");
        }
        if (userStorage.getById(userId) == null) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        filmStorage.addLike(id, userId);
    }

    @Override
    public void deleteLike(int id, int userId) {
        if (filmStorage.getById(id) == null) {
            throw new NotFoundException("Фильма с таким id не существует");
        }
        filmStorage.deleteLike(id, userId);
    }

    @Override
    public List<Film> getTopFilms(int count) {
        return filmStorage.getTopFilms(count);
    }
}