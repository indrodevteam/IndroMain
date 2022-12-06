package io.github.indroDevTeam.indroMain.data;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.model.Point;
import io.github.indroDevTeam.indroMain.model.Profile;
import io.github.indroDevTeam.indroMain.model.Rank;
import io.github.indroDevTeam.indroMain.utils.ChatUtils;

import java.io.File;
import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SqliteDataApi implements DataAPI {
    private final IndroMain plugin;

    public SqliteDataApi(IndroMain plugin) {
        this.plugin = plugin;
    }

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + File.separator + "data.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            conn.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS points (ownerId varchar(36), name varchar(16), desc varchar(255), x varchar(255), y varchar(255), z varchar(255), pitch varchar(255), yaw varcharZ(255), worldName varchar(255)");
            conn.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS profiles (userId varchar(36), rankId varchar(255), level int, currentXp int, nextXp int);");
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
        String sql = "SELECT * FROM profiles WHERE (userId = ?);";

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
        String sql = ""
        return false;
    }

    @Override
    public Optional<Point> getPoint(UUID ownerId, String name) {
        return Optional.empty();
    }

    @Override
    public List<Point> getPointByOwner(UUID ownerId) {
        return null;
    }

    @Override
    public List<Point> getAllPoints() {
        return null;
    }

    @Override
    public boolean updatePoint(UUID ownerId, String name, Point newPoint) {
        return false;
    }

    @Override
    public boolean deletePoint(UUID ownerId, String name) {
        return false;
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
