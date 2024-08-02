package ru.yandex.practicum.filmorate.dal.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseDbStorage;
import ru.yandex.practicum.filmorate.dal.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.dal.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Repository
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private JdbcTemplate jt;
    MpaDbStorage mpaDbStorage;
    GenreDbStorage genreDbStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Film> mapper, MpaDbStorage mpaDbStorage, GenreDbStorage genreDbStorage, JdbcTemplate jt) {
        super(jdbcTemplate, mapper);
        this.jt = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
        this.genreDbStorage = genreDbStorage;
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT f.id AS film_id, " +
                "f.name AS film_name, " +
                "f.description, f.releaseDate, f.duration, f.mpa_id, f.likes, " +
                "m.mpa_name AS mpa_name, g.genre_id, g.genre AS genre_name " +
                "FROM films f " +
                "LEFT JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genre fg ON f.id = fg.film_id " +
                "LEFT JOIN genres g ON fg.genre_id = g.genre_id";
        return jt.query(sql, new FilmResultSetExtractor());
    }

    @Override
    public Film getFilm(Long id) {
        String sql = "SELECT f.id AS film_id, " +
                "f.name AS film_name, " +
                "f.description, f.releaseDate, f.duration, f.mpa_id, f.likes, " +
                "m.mpa_name AS mpa_name, g.genre_id, g.genre AS genre_name " +
                "FROM films f " +
                "LEFT JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genre fg ON f.id = fg.film_id " +
                "LEFT JOIN genres g ON fg.genre_id = g.genre_id " +
                "WHERE f.id = " + id;
        return jt.query(sql, new FilmResultSetExtractor()).getFirst();
    }

    @Override
    public Film addFilm(Film film) {
        String sql = "INSERT INTO films (name, description, releaseDate, duration, likes, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?, (SELECT mpa_id FROM mpa WHERE mpa_id = ?))";
        long id = insert(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), 0,
                film.getMpa().getId());
        film.setId(id);
        film.setLikes(0);
        Optional<Mpa> mpa = mpaDbStorage.getMpa(film.getMpa().getId());
        if (mpa.isEmpty()) {
            log.warn("Рейтинг с id {} не найден в базе", film.getId());
            throw new ValidationException(String.format("Рейтинга с id = %d не существует", film.getMpa().getId()));
        }
        film.setMpa(mpa.orElse(null));
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            film.setGenres(new HashSet<Genre>());
        } else {
            Set<Genre> genres = film.getGenres();
            for (Genre genre : genres) {
                Optional<Genre> newGenre = genreDbStorage.getGenre(genre.getId());
                if (newGenre.isEmpty()) {
                    log.warn("Жанр с id {} не найден в базе", genre.getId());
                    throw new ValidationException(String.format("Жанра с id = %d не существует", genre.getId()));
                }
                Genre genreToAdd = newGenre.orElse(null);
                genre.setName(genreToAdd.getName());
            }
            addFilmGenre(film.getId(), film.getGenres());
            film.setGenres(genres);
        }
        return film;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        Film oldFilm = getFilm(newFilm.getId());
        String sql = "UPDATE films SET name = ?, description = ?, releasedate = ?, duration = ?, " +
                "mpa_id = ? WHERE id = ?";
        update(sql, newFilm.getName(), newFilm.getDescription(), newFilm.getReleaseDate(), newFilm.getDuration(),
                newFilm.getMpa().getId(), newFilm.getId());
        if (oldFilm.getGenres() != null && !oldFilm.getGenres().isEmpty()) {
            Set<Genre> genres = oldFilm.getGenres();
            for (int i = 0; i < genres.size(); i++) {
                deleteFilmGenre(oldFilm.getId());
            }
        }
        if (newFilm.getGenres() != null && !newFilm.getGenres().isEmpty()) {
            addFilmGenre(newFilm.getId(), newFilm.getGenres());
        }
        return newFilm;
    }

    @Override
    public void addLike(Long userId, Long filmId) {
        String sql = "INSERT INTO likes (user_Id, film_Id) VALUES (?, ?)";
        String sqlUpdateLikes = "UPDATE films SET likes = likes + 1 WHERE id = ?";
        insertNoKey(sql, userId, filmId);
        update(sqlUpdateLikes, filmId);
    }

    @Override
    public void removeLike(Long userId, Long filmId) {
        String sql = "DELETE from likes WHERE user_id = ? AND film_id = ?";
        String sqlDeleteLikes = "UPDATE films SET likes = likes - 1 WHERE id = ?";
        insertNoKey(sql, userId, filmId);
        update(sqlDeleteLikes, filmId);
    }

    @Override
    public List<Film> getPopularFilms(Integer limit) {
        String sql = "SELECT f.id AS film_id, " +
                "f.name AS film_name, " +
                "f.description, f.releaseDate, f.duration, f.mpa_id, f.likes, " +
                "m.mpa_name AS mpa_name, g.genre_id, g.genre AS genre_name " +
                "FROM films f " +
                "LEFT JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genre fg ON f.id = fg.film_id " +
                "LEFT JOIN genres g ON fg.genre_id = g.genre_id " +
                "ORDER BY f.likes DESC " +
                "LIMIT ?;";
        return findMany(sql, limit);
    }

    private void addFilmGenre(Long filmId, Set<Genre> genres) {
        for (Genre genre : genres) {
            String sql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
            insertNoKey(sql, filmId, genre.getId());
        }
    }

    private void deleteFilmGenre(Long filmId) {
        String sql = "DELETE FROM film_genre WHERE film_id = ?;";
        jt.update(sql, filmId);
    }
}
