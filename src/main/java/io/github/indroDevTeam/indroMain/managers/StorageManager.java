package io.github.indroDevTeam.indroMain.managers;

import java.util.HashMap;
import java.util.UUID;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.ranks.Rank;
import io.github.indroDevTeam.indroMain.storage.DBCore;
import io.github.indroDevTeam.indroMain.storage.SQLiteCore;

public final class StorageManager {

    private IndroMain plugin;
    private DBCore core;
    private HashMap<UUID, Rank> rankHashMap = new HashMap<>();

    /**
     *
     */
    public StorageManager() {
        plugin = IndroMain.getInstance();
        initiateDB();
        updateDatabase();
        importFromDatabase();
    }


    private void initiateDB() {
        core = new SQLiteCore(plugin.getDataFolder().getPath());
        if (core.checkConnection()) {

            plugin.getLogger().info(plugin.getConfigManager().getValue("lang.sqlite.connection.successful").toString());

        }
    }

}