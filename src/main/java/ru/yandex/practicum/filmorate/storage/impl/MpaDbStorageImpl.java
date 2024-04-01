package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Component
public class MpaDbStorageImpl implements MpaStorage {
    private final JdbcOperations jdbcOperations;

    public MpaDbStorageImpl(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<Mpa> getAll() {
        String sqlQuery = "select * from mpa";
        return jdbcOperations.query(sqlQuery, mpaRowMapper());
    }

    @Override
    public Mpa getById(int id) {
        String sqlQuery = "select * from mpa where rating_id = ?";
        List<Mpa> mpa = jdbcOperations.query(sqlQuery, mpaRowMapper(), id);
        if (mpa.size() != 1) {
            return null;
        }
        return mpa.get(0);
    }

    private RowMapper<Mpa> mpaRowMapper() {
        return (rs, rowNum) -> new Mpa(
                rs.getInt("rating_id"),
                rs.getString("rating_name"));
    }
}