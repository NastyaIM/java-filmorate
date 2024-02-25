package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private static int id = 1;

    @GetMapping
    public ResponseEntity<List<Film>> findAllFilms() {
        log.debug("Всего фильмов: " + films.size());
        return new ResponseEntity<>(new ArrayList<>(films.values()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (films.containsValue(film)) {
            log.warn("Фильм уже существует");
            throw new ValidationException("Фильм уже существует");
        }
        int filmId = getId();
        film.setId(filmId);
        log.debug("Будет добавлен фильм {} c id {}", film, filmId);
        films.put(film.getId(), film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            log.warn("Фильма с таким идентификатором не существует");
            throw new ValidationException("Фильма с таким идентификатором не существует");
        }
        log.debug("Будет обновлен фильм с id: {} на {}", film.getId(), film);
        films.put(film.getId(), film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    private int getId() {
        return id++;
    }
}
