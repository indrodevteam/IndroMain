package indrocraft.indrocraftplugin.dataManager;

import indrocraft.indrocraftplugin.Main;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;


public class SQLite {

    private JavaPlugin plugin;
    private Connection connection;

    public SQLite(Main main) {
        this.plugin = main;
    }


    public Connection getSQLConnection() {
        File file;
        file = new File(plugin.getDataFolder(), "indrocraft.db");
        plugin.getDataFolder().mkdirs();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: indrocraft.db");
            }
        }
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + file);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        try {
            PreparedStatement s = connection.prepareStatement("CREATE TABLE IF NOT EXISTS players " +
                    "(UUID VARCHAR(100),PRIMARY KEY (UUID))");
            s.executeUpdate();
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            Error.close(plugin, ex);
        }
    }
    
    public void closeConnection() {
    	try {
    		if (!(connection.isClosed())) {
        		connection.close();
        		connection = null;
        	} else {
        		connection = null;
        	}
    	} catch(Exception e) {
    		Error.close(plugin, e);
    	}
    }

}