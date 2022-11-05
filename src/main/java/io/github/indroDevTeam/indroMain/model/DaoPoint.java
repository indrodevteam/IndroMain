package io.github.indroDevTeam.indroMain.model;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.data.DataSourceFactory;
import io.github.indroDevTeam.indroMain.data.PointDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DaoPoint implements PointDao {
    private DaoPoint() {} // prevent initialisation

    private static class SingletonHelper {
        private static final DaoPoint INSTANCE = new DaoPoint();
    }

    public static DaoPoint getInstance() {
        return DaoPoint.SingletonHelper.INSTANCE;
    }

    @Override
    public Optional<Point> find(String id) throws SQLException {

        String sql = "SELECT point_id, owner_id, name, x, y, z, pitch, yaw, world_name FROM points WHERE point_id = ?;";

        String pointId = "";
        UUID ownerId = null;
        String name = "";
        double x = 0;
        double y = 0;
        double z = 0;
        float pitch = 0;
        float yaw = 0;
        String worldName = "";

        Connection conn = IndroMain.daso.getConnection();

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            pointId = resultSet.getString("point_id");
            ownerId = UUID.fromString(resultSet.getString("owner_id"));
            name = resultSet.getString("name");
            x = resultSet.getDouble("x");
            y = resultSet.getDouble("y");
            z = resultSet.getDouble("z");
            pitch = resultSet.getFloat("pitch");
            yaw = resultSet.getFloat("yaw");
            worldName = resultSet.getString("world_name");
        }

        return Optional.of(new Point(pointId, ownerId, name, x, y, z, pitch, yaw, worldName));
    }

    public Optional<Point> find(UUID ownerId, String name) throws SQLException {
        String sql = "SELECT point_id, owner_id, name, x, y, z, pitch, yaw, world_name FROM points WHERE owner_id = ? AND name = ?;";

        String pointId = "";
        UUID ownerUuid = null;
        String pointName = "";
        double x = 0;
        double y = 0;
        double z = 0;
        float pitch = 0;
        float yaw = 0;
        String worldName = "";

        Connection conn = IndroMain.daso.getConnection();

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, ownerId.toString());
        statement.setString(2, name);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            pointId = resultSet.getString("point_id");
            ownerUuid = UUID.fromString(resultSet.getString("owner_id"));
            pointName = resultSet.getString("name");
            x = resultSet.getDouble("x");
            y = resultSet.getDouble("y");
            z = resultSet.getDouble("z");
            pitch = resultSet.getFloat("pitch");
            yaw = resultSet.getFloat("yaw");
            worldName = resultSet.getString("world_name");
        }

        return Optional.of(new Point(pointId, ownerUuid, pointName, x, y, z, pitch, yaw, worldName));
    }

    public List<Point> findAll(UUID playerId) throws SQLException {
        List<Point> pointList = new ArrayList<>();
        String sql = "SELECT point_id, owner_id, name, x, y, z, pitch, yaw, world_name FROM points WHERE owner_id = ?;";

        Connection conn = IndroMain.daso.getConnection();

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, playerId.toString());
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String pointId = resultSet.getString("point_id");
            UUID ownerId = UUID.fromString(resultSet.getString("owner_id"));
            String name = resultSet.getString("name");
            double x = resultSet.getDouble("x");
            double y = resultSet.getDouble("y");
            double z = resultSet.getDouble("z");
            float pitch = resultSet.getFloat("pitch");
            float yaw = resultSet.getFloat("yaw");
            String worldName = resultSet.getString("world_name");

            Point point = new Point(pointId, ownerId, name, x, y, z, pitch, yaw, worldName);
            pointList.add(point);
        }
        return pointList;
    }

    @Override
    public List<Point> findAll() throws SQLException {
        List<Point> pointList = new ArrayList<>();
        String sql = "SELECT point_id, owner_id, name, x, y, z, pitch, yaw, world_name FROM points;";

        Connection conn = IndroMain.daso.getConnection();

        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String pointId = resultSet.getString("point_id");
            UUID ownerId = UUID.fromString(resultSet.getString("owner_id"));
            String name = resultSet.getString("name");
            double x = resultSet.getDouble("x");
            double y = resultSet.getDouble("y");
            double z = resultSet.getDouble("z");
            float pitch = resultSet.getFloat("pitch");
            float yaw = resultSet.getFloat("yaw");
            String worldName = resultSet.getString("world_name");

            Point point = new Point(pointId, ownerId, name, x, y, z, pitch, yaw, worldName);
            pointList.add(point);
        }
        return pointList;
    }

    @Override
    public boolean save(Point o) throws SQLException {
        String sql = "INSERT INTO points (point_id, owner_id, name, x, y, z, pitch, yaw, world_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        boolean rowInserted;
        Connection conn = IndroMain.daso.getConnection();

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, o.getPointId());
        statement.setString(2, o.getOwnerId().toString());
        statement.setString(3, o.getName());
        statement.setDouble(4, o.getX());
        statement.setDouble(5, o.getY());
        statement.setDouble(6, o.getZ());
        statement.setFloat(7, o.getPitch());
        statement.setFloat(8, o.getYaw());
        statement.setString(9, o.worldName);

        rowInserted = statement.executeUpdate() > 0;
        return rowInserted;
    }

    @Override
    public boolean update(Point o) throws SQLException {
        String sql = "UPDATE points SET point_id = ?, owner_id = ?, name = ?, x = ?, y = ?, z = ?, pitch = ?, yaw = ?, world_name = ? WHERE point_id = ?;";
        boolean rowUpdated;

        Connection conn = IndroMain.daso.getConnection();

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, o.getOwnerId().toString());
        statement.setString(2, o.getName());
        statement.setDouble(3, o.getX());
        statement.setDouble(4, o.getY());
        statement.setDouble(5, o.getZ());
        statement.setFloat(6, o.getPitch());
        statement.setFloat(7, o.getYaw());
        statement.setString(8, o.worldName);
        statement.setString(9, o.getPointId());

        rowUpdated = statement.executeUpdate() > 0;
        return rowUpdated;
    }

    @Override
    public boolean delete(Point o) throws SQLException {
        String sql = "DELETE FROM points WHERE point_id = ?;";
        boolean rowDeleted;

        Connection conn = IndroMain.daso.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, o.getPointId());

        rowDeleted = statement.executeUpdate() > 0;
        return rowDeleted;
    }
}
