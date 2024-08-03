package ru.yandex.practicum.filmorate.dal.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> implements GenreStorage {

    public GenreDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Genre> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Collection<Genre> getGenres() {
        String sql = "SELECT * FROM genres";
        return findMany(sql);
    }

    @Override
    public Optional<Genre> getGenre(Long id) {
        String sql = "SELECT * FROM genres WHERE genre_id = ?";
        return findOne(sql, id);
    }
}
