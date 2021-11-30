package indrocraft.indrocraftplugin.dataManager;

import indrocraft.indrocraftplugin.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private Main main;
    public MySQL(Main plugin) {this.main = plugin;}

    ConfigTools config = new ConfigTools(main, "config.yml");

    private String host = config.getConfig().getString("database.host");
    private String port = config.getConfig().getString("database.port");
    private String database = config.getConfig().getString("database.database");
    private String username = config.getConfig().getString("database.user");
    String pass = config.getConfig().getString("database.password");
    private String password = pass;


    private Connection connection;

    public boolean isConnected() {
        return (connection == null ? false : true);
    }

    public void connect() throws ClassNotFoundException, SQLException {
        String pass = config.getConfig().getString("database.password");
        if (pass.equalsIgnoreCase("passwordhere")){
            password = "";
        }

        if (!isConnected()) {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSl=false", username, password);
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}

