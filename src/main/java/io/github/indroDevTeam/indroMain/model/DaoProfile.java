package io.github.indrodevteam.indroMain.model;

import io.github.indrodevteam.indroMain.data.DataSourceFactory;
import io.github.indrodevteam.indroMain.data.ProfileDao;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DaoProfile implements ProfileDao {
    private DaoProfile() {} // prevent initialisation

    private static class SingletonHelper {
        private static final DaoProfile INSTANCE = new DaoProfile();
    }

    public static DaoProfile getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public Optional<Profile> find(UUID uuid) throws SQLException {

        String sql = "SELECT player_id, rankId, level, currentXp, nextXp FROM profiles WHERE player_id = ?;";
        UUID playerId = null;
        String rankId = "";
        int level = 0;
        int currentXp = 0;
        int nextXp = 0;
        Connection conn = DataSourceFactory.getConnection();

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, uuid.toString());
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            playerId = UUID.fromString(resultSet.getString("player_id"));
            rankId = resultSet.getString("rankId");
            level = resultSet.getInt("level");
            currentXp = resultSet.getInt("currentXp");
            nextXp = resultSet.getInt("nextXp");
        }
        return Optional.of(new Profile(playerId, rankId, level, currentXp, nextXp));
    }

    @Override
    public List<Profile> findAll() throws SQLException {
        List<Profile> profileList = new ArrayList<>();
        String sql = "SELECT player_id, rankId, level, currentXp, nextXp FROM profiles;";

        Connection conn = DataSourceFactory.getConnection();

        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            UUID playerId = UUID.fromString(resultSet.getString("player_id"));
            String rankId = resultSet.getString("rankId");
            int level = resultSet.getInt("level");
            int currentXp = resultSet.getInt("currentXp");
            int nextXp = resultSet.getInt("nextXp");

            Profile profile = new Profile(playerId, rankId, level, currentXp, nextXp);
            profileList.add(profile);
        }
        return profileList;
    }

    @Override
    public boolean save(Profile o) throws SQLException {
        String sql = "INSERT INTO profiles (player_id, rankId, level, currentXp, nextXp) VALUES (?, ?, ?, ?, ?);";
        boolean rowInserted;
        Connection conn = DataSourceFactory.getConnection();

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, o.getPlayerId().toString());
        statement.setString(2, o.getRankId());
        statement.setInt(3, o.getLevel());
        statement.setInt(4, o.getCurrentXp());
        statement.setInt(5, o.getNextXp());

        rowInserted = statement.executeUpdate() > 0;
        return rowInserted;
    }

    @Override
    public boolean update(Profile o) throws SQLException {
        String sql = "UPDATE profiles SET (player_id = ?, rankId = ?, level = ?, currentXp = ?, nextXp = ?) WHERE player_id = ?";
        boolean rowUpdated;

        Connection conn = DataSourceFactory.getConnection();

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, o.getPlayerId().toString());
        statement.setString(2, o.getRankId());
        statement.setInt(3, o.getLevel());
        statement.setInt(4, o.getCurrentXp());
        statement.setInt(5, o.getNextXp());

        rowUpdated = statement.executeUpdate() > 0;
        return rowUpdated;
    }

    @Override
    public boolean delete(Profile o) throws SQLException {
        String sql = "DELETE FROM profiles WHERE player_id = ?";
        boolean rowDeleted;

        Connection conn = DataSourceFactory.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, o.getPlayerId().toString());

        rowDeleted = statement.executeUpdate() > 0;
        return rowDeleted;
    }
}
