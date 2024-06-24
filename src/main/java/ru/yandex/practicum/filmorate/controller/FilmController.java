package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private final int maxLengthDescription = 200;
    private final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);
    private final String wrongNameMessage = "Название фильма не может быть пустым";
    private final String wrongDescriptionMessage = "Описание фильма должно быть меньше 200 символов";
    private final String wrongReleaseDateMessage = "Дата выхода фильма не может быть раньше" + minReleaseDate;
    private final String wrongDurationMessage = "Продолжительность фильма должна быть положительным числом";

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.trace("Начало добавления фильма");
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn(wrongNameMessage);
            throw new ValidationException(wrongNameMessage);
        }
        if (film.getDescription().length() > maxLengthDescription) {
            log.warn(wrongDescriptionMessage);
            throw new ValidationException(wrongDescriptionMessage);
        }
        if (film.getReleaseDate().isBefore(minReleaseDate)) {
            log.warn(wrongReleaseDateMessage);
            throw new ValidationException(wrongReleaseDateMessage);
        }
        if (film.getDuration() < 0) {
            log.warn(wrongDurationMessage);
            throw new ValidationException(wrongDurationMessage);
        }
        film.setId(getNextId());
        log.trace("Добавление фильма с уникальным идентификатором");
        films.put(film.getId(), film);
        log.trace("Новый фильм успешно добавлен");
        return film;
    }

    private int getNextId() {
        log.trace("Получение идентификатора");
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.trace("Начало обновления данных о фильме");
        Film oldFilm = films.get(newFilm.getId());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setDuration(newFilm.getDuration());
        oldFilm.setName(newFilm.getName());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        log.trace("Данные о фильме обновлены");
        return oldFilm;
    }
}
