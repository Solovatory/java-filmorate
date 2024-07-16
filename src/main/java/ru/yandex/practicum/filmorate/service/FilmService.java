package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long id, Long userId) {
        Film film = filmStorage.getFilm(id);
        User user = userStorage.getUser(userId);
        film.getLikes().add(user.getId());
        log.trace("Фильму {} поставлен лайк от пользователя {}", film.getName(), user.getName());
    }

    public void removeLike(Long id, Long userId) {
        Film film = filmStorage.getFilm(id);
        User user = userStorage.getUser(userId);
        film.getLikes().remove(user.getId());
        log.trace("Фильму {} удален лайк от пользователя {}", film.getName(), user.getName());
    }

    public List<Film> getMostPopularFilms(Long count) {
        log.trace("Показываем {} самых популярных фильмов", count);
        return filmStorage.getFilms()
                .stream()
                .sorted((film0, film1) -> Long.compare(film1.getLikes().size(), film0.getLikes().size()))
                .limit(count)
                .toList();
    }
}
