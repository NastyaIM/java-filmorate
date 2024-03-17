package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class BaseFilmService implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public BaseFilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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
        return film;
    }

    @Override
    public Film add(Film film) {
        if (filmStorage.getAll().contains(film)) {
            throw new AlreadyExistsException("Фильм уже существует");
        }
        return filmStorage.add(film);
    }

    @Override
    public Film update(Film film) {
        if (filmStorage.getById(film.getId()) == null) {
            throw new NotFoundException("Фильма с таким id не существует");
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
    public List<Integer> getAllLikes(int id) {
        if (filmStorage.getById(id) == null) {
            throw new NotFoundException("Фильма с таким id не существует");
        }
        return filmStorage.getAllLikes(id);
    }

    @Override
    public List<Film> getTopFilms(int count) {
        return filmStorage.getTopFilms(count);
    }
}
