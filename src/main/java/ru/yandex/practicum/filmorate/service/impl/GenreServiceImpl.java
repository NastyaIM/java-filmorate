package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreStorage genreStorage;

    public GenreServiceImpl(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Override
    public List<Genre> getAll() {
        return genreStorage.getAll();
    }

    @Override
    public Genre getById(int id) {
        Genre genre = genreStorage.getById(id);
        if (genre == null) {
            throw new NotFoundException("Жанра с таким id не существует");
        }
        return genre;
    }
}
