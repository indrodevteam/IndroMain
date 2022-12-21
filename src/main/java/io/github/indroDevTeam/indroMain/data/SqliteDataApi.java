package io.github.indroDevTeam.indroMain.data;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.model.Point;
import io.github.indroDevTeam.indroMain.model.Profile;
import io.github.indroDevTeam.indroMain.model.Rank;

import java.io.File;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SqliteDataApi implements DataAPI {
    private final IndroMain plugin;

    public SqliteDataApi(IndroMain plugin) throws SQLException {
        this.plugin = plugin;

        File file = new File(plugin.getDataFolder(),"data.db");
        if(!file.exists()) new File(plugin.getDataFolder().getPath()).mkdir();
        createTables();
    }

    private void createTables() {
        try (Connection conn = this.connect()) {
            conn.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS points (ownerId varchar(36), name varchar(16), desc text, x varchar(255), y varchar(255), z varchar(255), pitch varchar(255), yaw varchar(255), worldName varchar(255));");
            conn.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS profiles (userId varchar(36), rankId varchar(255), level int, currentXp int, nextXp int);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + File.separator + "data.db";
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
        }
        return conn;
    }



    @Override
    public boolean createProfile(Profile profile) {
        int updateStatus;
        String sql = "INSERT INTO profiles VALUES (?, ?, ?, ?, ?);";

        try (Connection conn = this.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, profile.getUserId().toString());
            stmt.setString(2, profile.getRankId());
            stmt.setInt(3, profile.getLevel());
            stmt.setInt(4, profile.getCurrentXp());
            stmt.setInt(5, profile.getNextXp());

            updateStatus = stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
            return false;
        }
        return updateStatus != 0;
    }

    @Override
    public Optional<Profile> getProfile(UUID userId) {
        String sql = "SELECT * FROM profiles WHERE userId = ?;";

        try (Connection conn = this.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId.toString());

            ResultSet rs = stmt.executeQuery();
            rs.next();

            UUID userId1 = UUID.fromString(rs.getString("userId"));
            String rankId = rs.getString("rankId");
            int level = rs.getInt("level");
            int currentXp = rs.getInt("currentXp");
            int nextXp = rs.getInt("nextXp");

            return Optional.of(new Profile(userId1, rankId, level, currentXp, nextXp));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateProfile(UUID userId, Profile newProfile) {
        int updateStatus;
        String sql = "UPDATE profiles SET userId = ?, rankId = ?, level = ?, currentXp = ?, nextXp = ? WHERE userId = ?;";

        try (Connection conn = this.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, newProfile.getUserId().toString());
            stmt.setString(2, newProfile.getRankId());
            stmt.setInt(3, newProfile.getLevel());
            stmt.setInt(4, newProfile.getCurrentXp());
            stmt.setInt(5, newProfile.getNextXp());

            stmt.setString(6, userId.toString());

            updateStatus = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return updateStatus != 0;
    }

    @Override
    public boolean deleteProfile(UUID userId) {
        int updateStatus;
        String sql = "DELETE FROM profiles WHERE userId = ?";

        try (Connection conn = this.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, userId.toString());

            updateStatus = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return updateStatus != 0;
    }

    @Override
    public boolean createPoint(Point point) {
        int updateStatus;
        String sql = "INSERT INTO points VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = this.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, point.getOwnerId().toString());
            stmt.setString(2, point.getName());
            stmt.setString(3, point.getDesc());
            stmt.setString(4, String.valueOf(point.getX()));
            stmt.setString(5, String.valueOf(point.getY()));
            stmt.setString(6, String.valueOf(point.getZ()));
            stmt.setString(7, String.valueOf(point.getPitch()));
            stmt.setString(8, String.valueOf(point.getYaw()));
            stmt.setString(9, String.valueOf(point.getWorldName()));

            updateStatus = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return updateStatus != 0;
    }

    @Override
    public Optional<Point> getPoint(UUID ownerId, String name) {
        String sql = "SELECT * FROM points WHERE ownerId = ? AND name = ?;";

        try (Connection conn = this.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, ownerId.toString());
            stmt.setString(2, name);

            ResultSet rs = stmt.executeQuery();

            UUID ownerId1 = UUID.fromString(rs.getString("ownerId"));
            String name1 = rs.getString("name");
            String desc = rs.getString("desc");
            double x = Double.parseDouble(rs.getString("x"));
            double y = Double.parseDouble(rs.getString("y"));
            double z = Double.parseDouble(rs.getString("z"));
            float pitch = Float.parseFloat(rs.getString("pitch"));
            float yaw = Float.parseFloat(rs.getString("yaw"));
            String worldName = rs.getString("worldName");

            return Optional.of(new Point(ownerId1, name1, desc, x, y, z, pitch, yaw, worldName));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Point> getPointByOwner(UUID ownerId) {
        List<Point> points = new LinkedList<>();
        String sql = "SELECT * FROM points WHERE ownerId = ?;";

        try (Connection conn = this.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, ownerId.toString());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { // return the first row in the SQL query
                UUID ownerId1 = UUID.fromString(rs.getString("ownerId"));
                String name1 = rs.getString("name");
                String desc = rs.getString("desc");
                double x = Double.parseDouble(rs.getString("x"));
                double y = Double.parseDouble(rs.getString("y"));
                double z = Double.parseDouble(rs.getString("z"));
                float pitch = Float.parseFloat(rs.getString("pitch"));
                float yaw = Float.parseFloat(rs.getString("yaw"));
                String worldName = rs.getString("worldName");

                points.add(new Point(ownerId1, name1, desc, x, y, z, pitch, yaw, worldName));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return points;
    }

    @Override
    public List<Point> getAllPoints() {
        List<Point> points = new LinkedList<>();
        String sql = "SELECT * FROM points;";

        try (Connection conn = this.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { // return the first row in the SQL query
                UUID ownerId1 = UUID.fromString(rs.getString("ownerId"));
                String name1 = rs.getString("name");
                String desc = rs.getString("desc");
                double x = Double.parseDouble(rs.getString("x"));
                double y = Double.parseDouble(rs.getString("y"));
                double z = Double.parseDouble(rs.getString("z"));
                float pitch = Float.parseFloat(rs.getString("pitch"));
                float yaw = Float.parseFloat(rs.getString("yaw"));
                String worldName = rs.getString("worldName");

                points.add(new Point(ownerId1, name1, desc, x, y, z, pitch, yaw, worldName));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return points;
    }

    @Override
    public boolean updatePoint(UUID ownerId, String name, Point newPoint) {
        int updateStatus;
        String sql = "UPDATE points SET ownerId = ?, name = ?, desc = ?, x = ?, y = ?, z = ?, pitch = ?, yaw = ?, worldName = ? WHERE ownerId = ? AND name = ?;";

        try (Connection conn = this.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, newPoint.getOwnerId().toString());
            stmt.setString(2, newPoint.getName());
            stmt.setString(3, newPoint.getDesc());
            stmt.setString(4, String.valueOf(newPoint.getX()));
            stmt.setString(5, String.valueOf(newPoint.getY()));
            stmt.setString(6, String.valueOf(newPoint.getPitch()));
            stmt.setString(7, String.valueOf(newPoint.getYaw()));
            stmt.setString(8, newPoint.getWorldName());

            stmt.setString(9, ownerId.toString());
            stmt.setString(10, name);

            updateStatus = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return updateStatus != 0;
    }

    @Override
    public boolean deletePoint(UUID ownerId, String name) {
        int updateStatus;
        String sql = "DELETE FROM points WHERE ownerId = ? AND name = ?";

        try (Connection conn = this.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, ownerId.toString());
            stmt.setString(2, name);

            updateStatus = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return updateStatus != 0;
    }

    @Override
    public boolean createRank(Rank rank) {
        return false;
    }

    @Override
    public Optional<Rank> getRank(String name) {
        return Optional.empty();
    }

    @Override
    public boolean updateRank(String name, Rank newRank) {
        return false;
    }

    @Override
    public boolean deleteRank(String name) {
        return false;
    }
}
