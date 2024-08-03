package ru.yandex.practicum.filmorate.dal.film;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("releasedate").toLocalDate());
        film.setDuration(resultSet.getInt("duration"));
        film.setLikes(resultSet.getInt("likes"));
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getInt("mpa_id"));
        mpa.setName(resultSet.getString("mpa_name"));
        film.setMpa(mpa);
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("genre_id"));
        genre.setName(resultSet.getString("genre"));
        Set<Genre> genres = new HashSet<>();
        genres.add(genre);
        film.setGenres(genres);
        return film;
    }
}

