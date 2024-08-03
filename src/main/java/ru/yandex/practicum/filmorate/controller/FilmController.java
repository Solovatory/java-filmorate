package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private FilmService filmService;
    private final String likePath = "/{id}/like/{user-id}";
    private final String userIdPath = "user-id";

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }


    @GetMapping
    public Collection<FilmDto> findAll() {
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public FilmDto findById(@PathVariable Long id) {
        return filmService.findById(id);
    }

    @PostMapping
    public FilmDto create(@Valid @RequestBody FilmRequest request) {
        return filmService.addFilm(request);
    }


    @PutMapping
    public FilmDto update(@Valid @RequestBody FilmRequest request) {
        return filmService.updateFilm(request);
    }

    @PutMapping(likePath)
    public void like(@PathVariable Long id, @PathVariable(userIdPath) Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping(likePath)
    public void unlike(@PathVariable Long id, @PathVariable(userIdPath) Long userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<FilmDto> getPopularFilms(@RequestParam(value = "count", defaultValue = "10", required = false)
                                               Integer count) {
        return filmService.getMostPopularFilms(count);
    }

}
