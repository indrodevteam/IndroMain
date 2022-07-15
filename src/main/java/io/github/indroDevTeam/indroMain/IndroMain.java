package io.github.indroDevTeam.indroMain;

import io.github.indroDevTeam.indroMain.managers.ConfigManager;
import io.github.indroDevTeam.indroMain.storage.DBCore;
import io.github.indroDevTeam.indroMain.storage.SQLiteCore;
import lombok.val;

import java.util.HashMap;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class IndroMain extends JavaPlugin {
    private static IndroMain instance;
    private ConfigManager configManager;
    private DBCore dataStorage;
    private static final Logger logger = Logger.getLogger("Minecraft");

    public static Logger getLog() {
        return logger;
    }

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager();
        dataStorage = new SQLiteCore();


        getLogger().info("Multithreading: " + IndroMain.getInstance().getConfigManager().getUseThreads());
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Class-Type Methods
    ///////////////////////////////////////////////////////////////////////////

    public static IndroMain getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    private void initDatabase() {

    }
}