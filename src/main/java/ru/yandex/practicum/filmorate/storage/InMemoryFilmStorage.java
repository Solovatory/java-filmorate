package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private final int maxLengthDescription = 200;
    private final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);
    private final String wrongNameMessage = "Название фильма не может быть пустым";
    private final String wrongDescriptionMessage = "Описание фильма должно быть меньше 200 символов";
    private final String wrongReleaseDateMessage = "Дата выхода фильма не может быть раньше" + minReleaseDate;
    private final String wrongDurationMessage = "Продолжительность фильма должна быть положительным числом";

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Film getFilm(Long id) {
        log.trace("Начало поиска фильма по id = {}", id);
        Film film = films.get(id);
        if (film == null) {
            log.warn("Фильм с id = {} не найден", id);
            throw new NotFoundException("Фильм не найден");
        } else {
            log.trace("Фильм с id = {} найден", id);
            return film;
        }
    }

    @Override
    public Film addFilm(Film film) {
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

    @Override
    public Film updateFilm(Film newFilm) {
        log.trace("Начало обновления данных о фильме");
        Film oldFilm = films.get(newFilm.getId());
        if (oldFilm == null) {
            throw new NotFoundException("Такого фильма не существует");
        } else {
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setDuration(newFilm.getDuration());
            oldFilm.setName(newFilm.getName());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            log.trace("Данные о фильме обновлены");
            return oldFilm;
        }
    }

    private Long getNextId() {
        log.trace("Получение уникального идентификатора для фильма");
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
