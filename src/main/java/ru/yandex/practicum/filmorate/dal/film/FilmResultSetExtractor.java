package ru.yandex.practicum.filmorate.dal.film;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmResultSetExtractor implements ResultSetExtractor<List<Film>> {

    @Override
    public List<Film> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<Long, Film> filmMap = new HashMap<>();
        while (resultSet.next()) {
            Long filmId = resultSet.getLong("film_id");
            Film film = filmMap.get(filmId);
            if (film == null) {
                film = new Film();
                film.setId(filmId);
                film.setName(resultSet.getString("film_name"));
                film.setDescription(resultSet.getString("description"));
                film.setReleaseDate(resultSet.getDate("releaseDate").toLocalDate());
                film.setDuration(resultSet.getInt("duration"));
                film.setLikes(resultSet.getInt("likes"));
                Mpa mpa = new Mpa();
                mpa.setId(resultSet.getInt("mpa_id"));
                mpa.setName(resultSet.getString("mpa_name"));
                film.setMpa(mpa);
                film.setGenres(new HashSet<>());
                filmMap.put(filmId, film);
            }
            int genreId = resultSet.getInt("genre_id");
            if (genreId > 0) {
                Genre genre = new Genre();
                genre.setId(genreId);
                genre.setName(resultSet.getString("genre_name"));
                film.getGenres().add(genre);
            }
        }
        return new ArrayList<>(filmMap.values());
    }
}

