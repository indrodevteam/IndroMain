package io.github.indrodevteam.indroMain.model;

import io.github.indrodevteam.indroMain.data.PointDao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DaoPoint implements PointDao {
    private DaoPoint() {} // prevent initialisation

    private static class SingletonHelper {
        private static final DaoPoint INSTANCE = new DaoPoint();
    }

    public static DaoPoint getInstance() {
        return DaoPoint.SingletonHelper.INSTANCE;
    }

    @Override
    public Optional<Point> find(String s) throws SQLException {
        return Optional.empty();
    }

    @Override
    public List<Point> findAll() throws SQLException {
        return null;
    }

    @Override
    public boolean save(Point o) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Point o) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(Point o) throws SQLException {
        return false;
    }
}
