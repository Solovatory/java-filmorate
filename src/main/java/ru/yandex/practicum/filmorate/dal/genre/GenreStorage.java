package ru.yandex.practicum.filmorate.dal.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreStorage {
    Collection<Genre> getGenres();

    Optional<Genre> getGenre(Long id);
}
