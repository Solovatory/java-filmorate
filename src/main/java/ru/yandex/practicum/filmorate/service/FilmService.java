package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.film.FilmStorage;
import ru.yandex.practicum.filmorate.dal.user.UserStorage;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final GenreService genreService;
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private MpaService mpaService;
    private final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);
    private final String wrongReleaseDateMessage = "Дата выхода фильма не может быть раньше" + minReleaseDate;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, MpaService mpaService, GenreService genreService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaService = mpaService;
        this.genreService = genreService;
    }

    public Collection<FilmDto> getFilms() {
        log.info("Получение списка всех фильмов");
        return filmStorage.getFilms().stream().map(FilmMapper::mapToFilmDto).toList();
    }

    public FilmDto findById(Long id) {
        log.info("Получение фильма по id = {}", id);
        Film film = getFilm(id);
        log.trace("Получен фильм {} с id = {}", film, id);
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto addFilm(FilmRequest request) {
        log.info("Добавление нового фильма");
        Film film = FilmMapper.mapToFilm(request);
        if (film.getReleaseDate().isBefore(minReleaseDate)) {
            log.warn("У фильма указана дата до {}", minReleaseDate);
            throw new ValidationException(wrongReleaseDateMessage);
        }
        log.trace("Добавлен новый фильм {}", film);
        return FilmMapper.mapToFilmDto(filmStorage.addFilm(film));
    }

    public FilmDto updateFilm(FilmRequest request) {
        log.trace("Обновление фильма {}", request);
        Film newFilm = FilmMapper.mapToFilm(request);
        if (newFilm.getReleaseDate().isBefore(minReleaseDate)) {
            log.warn("У фильма указана дата до {}", minReleaseDate);
            throw new ValidationException(wrongReleaseDateMessage);
        }
        return FilmMapper.mapToFilmDto(filmStorage.updateFilm(newFilm));
    }

    public void addLike(Long id, Long userId) {
        Film film = getFilm(id);
        User user = getUser(userId);
        filmStorage.addLike(user.getId(), film.getId());
        log.trace("Фильму {} поставлен лайк от пользователя {}", film.getName(), user.getName());
    }

    public void removeLike(Long id, Long userId) {
        Film film = getFilm(id);
        User user = getUser(userId);
        filmStorage.removeLike(user.getId(), film.getId());
        log.trace("Фильму {} удален лайк от пользователя {}", film.getName(), user.getName());
    }

    public List<FilmDto> getMostPopularFilms(Integer count) {
        log.trace("Показываем {} самых популярных фильмов", count);
        return filmStorage.getPopularFilms(count).stream().map(FilmMapper::mapToFilmDto).toList();
    }

    private Film getFilm(Long id) {
        return filmStorage.getFilm(id);
    }

    private User getUser(Long id) {
        return userStorage.getUser(id).orElseThrow(()
                -> new NotFoundException(String.format("Пользователь с идентификатором = '%s' не найден", id)));
    }
}
