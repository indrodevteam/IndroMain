package io.github.indroDevTeam.indroMain.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.model.Point;
import io.github.indroDevTeam.indroMain.model.Profile;
import io.github.indroDevTeam.indroMain.model.Rank;

import java.io.*;
import java.sql.*;
import java.util.*;

public class SqliteDataApi implements DataAPI {
    private final IndroMain plugin;
    private List<Rank> ranks;

    public SqliteDataApi(IndroMain plugin) throws SQLException, IOException {
        this.plugin = plugin;

        File file = new File(plugin.getDataFolder(),"data.db");
        if(!file.exists()) new File(plugin.getDataFolder().getPath()).mkdir();
        createTables();

        // create ranks here
        ranks = new ArrayList<>();
        loadRanks();
    }

    private void createTables() {
        try (Connection conn = this.connect()) {
            conn.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS points (ownerId varchar(36), name varchar(16), x varchar(255), y varchar(255), z varchar(255), pitch varchar(255), yaw varchar(255), worldName varchar(255));");
            conn.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS profiles (userId varchar(36), rankName varchar(255), level int, currentXp int, nextXp int);");
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
    public void deinit() {
        try {
            saveRanks();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean createProfile(Profile profile) {
        int updateStatus;
        String sql = "INSERT INTO profiles VALUES (?, ?, ?, ?, ?);";

        try (Connection conn = this.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, profile.getUserId().toString());
            stmt.setString(2, profile.getRankName());
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
            if (rs.isClosed() || !rs.next()) {
                return Optional.empty();
            }

            UUID userId1 = UUID.fromString(rs.getString("userId"));
            String rankName = rs.getString("rankName");
            int level = rs.getInt("level");
            int currentXp = rs.getInt("currentXp");
            int nextXp = rs.getInt("nextXp");

            // close files
            rs.close();
            stmt.close();


            Rank rank = getRank(rankName).isPresent() ? getRank(rankName).get() : null;
            if (rank == null) {
                return Optional.empty();
            }
            return Optional.of(new Profile(userId1, rank, level, currentXp, nextXp));
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean updateProfile(UUID userId, Profile newProfile) {
        int updateStatus;
        String sql = "UPDATE profiles SET userId = ?, rankName = ?, level = ?, currentXp = ?, nextXp = ? WHERE userId = ?;";

        try (Connection conn = this.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, newProfile.getUserId().toString());
            stmt.setString(2, newProfile.getRankName());
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
        String sql = "INSERT INTO points VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = this.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, point.getOwnerId().toString());
            stmt.setString(2, point.getName());
            stmt.setString(3, String.valueOf(point.getX()));
            stmt.setString(4, String.valueOf(point.getY()));
            stmt.setString(5, String.valueOf(point.getZ()));
            stmt.setString(6, String.valueOf(point.getPitch()));
            stmt.setString(7, String.valueOf(point.getYaw()));
            stmt.setString(8, String.valueOf(point.getWorldName()));

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
            if (rs.isClosed() || !rs.next()) {
                return Optional.empty();
            }

            UUID ownerId1 = UUID.fromString(rs.getString("ownerId"));
            String name1 = rs.getString("name");
            double x = Double.parseDouble(rs.getString("x"));
            double y = Double.parseDouble(rs.getString("y"));
            double z = Double.parseDouble(rs.getString("z"));
            float pitch = Float.parseFloat(rs.getString("pitch"));
            float yaw = Float.parseFloat(rs.getString("yaw"));
            String worldName = rs.getString("worldName");

            return Optional.of(new Point(ownerId1, name1, x, y, z, pitch, yaw, worldName));
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
                double x = Double.parseDouble(rs.getString("x"));
                double y = Double.parseDouble(rs.getString("y"));
                double z = Double.parseDouble(rs.getString("z"));
                float pitch = Float.parseFloat(rs.getString("pitch"));
                float yaw = Float.parseFloat(rs.getString("yaw"));
                String worldName = rs.getString("worldName");

                points.add(new Point(ownerId1, name1, x, y, z, pitch, yaw, worldName));
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
            while (rs.next() && !rs.isClosed()) { // return the first row in the SQL query
                UUID ownerId1 = UUID.fromString(rs.getString("ownerId"));
                String name1 = rs.getString("name");
                double x = Double.parseDouble(rs.getString("x"));
                double y = Double.parseDouble(rs.getString("y"));
                double z = Double.parseDouble(rs.getString("z"));
                float pitch = Float.parseFloat(rs.getString("pitch"));
                float yaw = Float.parseFloat(rs.getString("yaw"));
                String worldName = rs.getString("worldName");

                points.add(new Point(ownerId1, name1, x, y, z, pitch, yaw, worldName));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return points;
    }

    @Override
    public boolean updatePoint(UUID ownerId, String name, Point newPoint) {
        int updateStatus;
        String sql = "UPDATE points SET ownerId = ?, name = ?, x = ?, y = ?, z = ?, pitch = ?, yaw = ?, worldName = ? WHERE ownerId = ? AND name = ?;";

        try (Connection conn = this.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, newPoint.getOwnerId().toString());
            stmt.setString(2, newPoint.getName());
            stmt.setString(3, String.valueOf(newPoint.getX()));
            stmt.setString(4, String.valueOf(newPoint.getY()));
            stmt.setString(5, String.valueOf(newPoint.getPitch()));
            stmt.setString(6, String.valueOf(newPoint.getYaw()));
            stmt.setString(7, newPoint.getWorldName());

            stmt.setString(8, ownerId.toString());
            stmt.setString(9, name);

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

    //TODO: Implement Rank System...
    @Override
    public boolean createRank(Rank rank) {
        ranks.add(rank);
        return true;
    }

    @Override
    public Optional<Rank> getRank(String name) {
        for (Rank rank : ranks) {
            if (rank.getName().equals(name)) {
                return Optional.of(rank);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean updateRank(String name, Rank newRank) {
        for (Rank rank : ranks) {
            if (rank.getName().equals(name)) {
                rank.setName(newRank.getName());
                rank.setChatTag(newRank.getChatTag());
                rank.setTabTag(newRank.getTabTag());

                rank.setWarpCap(newRank.getWarpCap());
                rank.setWarpDelay(newRank.getWarpDelay());
                rank.setWarpCooldown(newRank.getWarpCooldown());
                rank.setMaxDistance(newRank.getMaxDistance());
                rank.setCrossWorldPermitted(newRank.isCrossWorldPermitted());

                rank.setAdvanceRequired(newRank.getAdvanceRequired());
                rank.setNextRanks(newRank.getNextRanks());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteRank(String name) {
        return ranks.removeIf(rank -> rank.getName().equalsIgnoreCase(name));
    }

    @Override
    public List<Rank> getAllRanks() {
        return ranks;
    }

    public void saveRanks() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(plugin.getDataFolder(),"ranks.json");
        if (!file.exists()) {
            file.getParentFile().mkdir();
            file.createNewFile();
        }

        Writer writer = new FileWriter(file, false);
        gson.toJson(ranks, writer);
        writer.flush();
        writer.close();
    }

    public void loadRanks() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(plugin.getDataFolder(),"ranks.json");
        if (!file.exists()) {
            loadFromResource();
            return;
        }
        Rank[] model = gson.fromJson(new FileReader(file), Rank[].class);
        ranks = new ArrayList<>(Arrays.asList(model));
    }

    private void loadFromResource() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(IndroMain.getInstance().getDataFolder() + File.separator + "ranks.json");
        if (file.exists()) {return;}

        InputStream rankStream = IndroMain.getInstance().getResource("ranks.json");
        assert rankStream != null;
        InputStreamReader inputStreamReader = new InputStreamReader(rankStream);
        ranks = new ArrayList<>(Arrays.asList(gson.fromJson(inputStreamReader, Rank[].class)));
        saveRanks();
    }
}
