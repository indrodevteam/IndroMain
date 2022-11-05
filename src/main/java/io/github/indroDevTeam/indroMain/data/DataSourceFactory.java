package io.github.indroDevTeam.indroMain.data;

import io.github.indroDevTeam.indroMain.IndroMain;

import java.io.File;
import java.sql.*;
import java.util.logging.Logger;

public class DataSourceFactory {
    private Connection connection;
    private static final Logger LOGGER = IndroMain.getInstance().getLogger();

    public Connection getConnection() throws SQLException {
        if (connection != null){
            return connection;
        }

        //Try to connect to the SQLite database running locally
        String url = "jdbc:sqlite:" + IndroMain.getInstance().getDataFolder().getAbsolutePath() + File.separator + "data.db";
        Connection connection = DriverManager.getConnection(url);

        this.connection = connection;
        LOGGER.info("Connected to database.");
        return connection;
    }

    public void initDatabase() throws SQLException {
        Statement statement = getConnection().createStatement();

        //Create the tables
        String profileSql = "CREATE TABLE IF NOT EXISTS profiles (player_id varchar(255), rank_id varchar(255), level int, current_xp int, next_xp int);";
        String pointSql = "CREATE TABLE IF NOT EXISTS points (point_id varchar(255), owner_id varchar(255), name varchar(255), x double(12, 9), y double(12, 9), z double(12, 9), pitch float(10), yaw float(10), world_name varchar(255));";

        statement.execute(profileSql);
        statement.execute(pointSql);
        statement.close();
    }
}
