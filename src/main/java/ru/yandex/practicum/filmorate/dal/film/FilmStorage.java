package ru.yandex.practicum.filmorate.dal.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Collection<Film> getFilms();

    Film getFilm(Long id);

    Film addFilm(Film film);

    Film updateFilm(Film newFilm);

    void addLike(Long userId, Long filmId);

    void removeLike(Long userId, Long filmId);

    List<Film> getPopularFilms(Integer limit);
}
