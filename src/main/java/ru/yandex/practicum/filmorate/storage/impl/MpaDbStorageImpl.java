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
    private static final String SQL_GET_ALL = "select * from mpa";
    private static final String SQL_GET_BY_ID = "select * from mpa where rating_id = ?";

    public MpaDbStorageImpl(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<Mpa> getAll() {
        return jdbcOperations.query(SQL_GET_ALL, mpaRowMapper());
    }

    @Override
    public Mpa getById(int id) {
        List<Mpa> mpa = jdbcOperations.query(SQL_GET_BY_ID, mpaRowMapper(), id);
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