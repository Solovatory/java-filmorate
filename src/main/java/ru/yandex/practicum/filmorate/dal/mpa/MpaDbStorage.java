package ru.yandex.practicum.filmorate.dal.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseDbStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

@Repository
public class MpaDbStorage extends BaseDbStorage<Mpa> implements MpaStorage {

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Mpa> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Collection<Mpa> getMpas() {
        String sql = "select * from mpa";
        return findMany(sql);
    }

    @Override
    public Optional<Mpa> getMpa(int id) {
        String sql = "select * from mpa where mpa_id = ?";
        return findOne(sql, id);
    }
}
