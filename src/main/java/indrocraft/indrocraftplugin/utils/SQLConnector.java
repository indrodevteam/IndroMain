package indrocraft.indrocraftplugin.utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class SQLConnector {
    /*
     *  +-----------------------------------------------+
     *  |                 SQL Connector:                |
     *  |   This section connects to the database and   |
     *  |    sets the credentials for the connection    |
     *  +-----------------------------------------------+
     */

    public Connection connection;
    private final Plugin PLUGIN;
    private final boolean USESQLITE;
    private final String PASSWORD;
    private final String HOST;
    private final String PORT;
    private final String DATABASE;
    private final String USERNAME;

    public boolean isUseSQLite() {
        return USESQLITE;
    }

    /**
     * @param database What is the name of the database you want to connect to?
     * @param host     Where is the database being hosted? If it's on this machine set this parameter to localhost.
     * @param port     What port is the server running on, the default port for MySQL is 3306.
     * @param username What user do you want to access the database as? Should be set to 'root' if possible.
     * @param password What password does that username use? If none just leave as an empty string "".
     */
    public SQLConnector(String database, String host, String port, String username, String password,
                        boolean useSQLite, Plugin plugin) {
        this.PASSWORD = password;
        this.USERNAME = username;
        this.DATABASE = database;
        this.HOST = host;
        this.PORT = port;

        this.USESQLITE = useSQLite;
        this.PLUGIN = plugin;
    }

    public Connection getMySQLConnection() {
        connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + this.HOST + ":" + this.PORT + "/" + this.DATABASE + "?allowPublicKeyRetrieval=true&useSSL=false",
                    USERNAME,
                    PASSWORD);
        } catch (SQLException e) {
            printSQLException(e);
            return null;
        }
        return connection;
    }

    public void closeConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public Connection getSQLiteConnection() {
        if(!this.PLUGIN.getDataFolder().exists()) {
            try {
                this.PLUGIN.getDataFolder().mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File dataFolder = new File(PLUGIN.getDataFolder(), DATABASE + ".db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                PLUGIN.getLogger().log(Level.SEVERE, "File write error: "+DATABASE+".db");
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            PLUGIN.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            PLUGIN.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                //e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException)e).getSQLState() + "\n");
                System.err.println("Error Code: " + ((SQLException)e).getErrorCode() + "\n");
                System.err.println("Message: " + e.getMessage() + "\n");
                System.err.println("error at line: " + e.getStackTrace()[e.getStackTrace().length-1] + "\n");
                System.err.println("The database is not connected! please ensure that the login credentials are " +
                        "correct and the database is running!");

                /*Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }*/
            }
        }
    }
}
