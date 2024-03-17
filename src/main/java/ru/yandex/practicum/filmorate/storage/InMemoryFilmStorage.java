package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final Map<Integer, Set<Integer>> likes = new HashMap<>();
    private int id = 1;

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getById(int id) {
        return films.get(id);
    }

    @Override
    public Film add(Film film) {
        int filmId = generateId();
        film.setId(filmId);
        likes.put(filmId, new HashSet<>());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void addLike(int id, int userId) {
        likes.get(id).add(userId);
    }

    @Override
    public void deleteLike(int id, int userId) {
        likes.get(id).remove(userId);
    }

    @Override
    public List<Integer> getAllLikes(int id) {
        return new ArrayList<>(likes.get(id));
    }

    @Override
    public List<Film> getTopFilms(int count) {
        List<Integer> topFilmsIds = likes.entrySet().stream()
                .sorted((f1, f2) -> f2.getValue().size() - f1.getValue().size())
                .map(Map.Entry::getKey)
                .limit(count)
                .collect(Collectors.toList());
        return getFilms(topFilmsIds);
    }

    private int generateId() {
        return id++;
    }

    private List<Film> getFilms(List<Integer> ids) {
        List<Film> films = new ArrayList<>();
        for (Integer i : ids) {
            films.add(getById(i));
        }
        return films;
    }
}
