package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    List<Genre> getAll();

    Genre getById(int id);

    Set<Genre> getByIds(List<Integer> ids);

    public Set<Genre> getAllFilmGenres(int filmId);
}