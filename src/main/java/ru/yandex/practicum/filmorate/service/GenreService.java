package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.genre.GenreStorage;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    private static final Logger log = LoggerFactory.getLogger(GenreService.class);
    private GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<GenreDto> getAllGenres() {
        log.info("Получение списка всех жанров из базы");
        return genreStorage.getGenres().stream().map(GenreMapper::mapToGenreDto).toList();
    }

    public GenreDto getGenreById(long id) {
        log.trace("Получения жанра из базы по id = {}", id);
        Optional<Genre> genreOptional = genreStorage.getGenre(id);
        if (genreOptional.isEmpty()) {
            log.warn("Жанр с id = {} не найден в базе", id);
            throw new NotFoundException(String.format("Жанра с id = %d не существует", id));
        }
        Genre genre = genreOptional.get();
        log.trace("Из базы получен жанр {}", genre);
        return GenreMapper.mapToGenreDto(genre);
    }
}
