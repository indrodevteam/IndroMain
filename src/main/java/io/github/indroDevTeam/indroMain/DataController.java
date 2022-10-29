package io.github.indrodevteam.indroMain;

import io.github.indrodevteam.indroMain.model.DaoProfile;

public class DataController {
    private DaoProfile daoProfile = DaoProfile.getInstance();

    public DataController() {
        //TODO: Add table creation code
        String profileSql = "CREATE TABLE profiles (player_id varchar(255), rank_id varchar(255), level int, current_xp int, next_xp int);";
        String pointSql = "CREATE TABLE points (";
    }
}
