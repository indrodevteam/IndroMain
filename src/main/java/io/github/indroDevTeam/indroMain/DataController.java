package io.github.indroDevTeam.indroMain;

import io.github.indroDevTeam.indroMain.data.DataSourceFactory;
import io.github.indroDevTeam.indroMain.model.DaoPoint;
import io.github.indroDevTeam.indroMain.model.DaoProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataController {
    private final DaoProfile daoProfile;
    private final DaoPoint daoPoint;

    public DataController() throws SQLException {
        //TODO: Add table creation code
        daoPoint = DaoPoint.getInstance();
        daoProfile = DaoProfile.getInstance();

        Connection conn = DataSourceFactory.getConnection();

        String profileSql = "CREATE TABLE IF NOT EXISTS profiles (player_id varchar(255), rank_id varchar(255), level int, current_xp int, next_xp int);";
        String pointSql = "CREATE TABLE IF NOT EXISTS points (point_id varchar(255), owner_id varchar(255), name varchar(255), x double(12, 9), y double(12, 9), z double(12, 9), pitch float(10), yaw float(10), world_name varchar(255));";

        conn.prepareStatement(profileSql).executeUpdate();
        conn.prepareStatement(pointSql).executeUpdate();
    }

    public DaoProfile getDaoProfile() {
        return daoProfile;
    }

    public DaoPoint getDaoPoint() {
        return daoPoint;
    }
}
