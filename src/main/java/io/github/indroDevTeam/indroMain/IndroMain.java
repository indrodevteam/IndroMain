package io.github.indroDevTeam.indroMain;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class IndroMain extends JavaPlugin {
    private FileConfiguration config;
    private static IndroMain instance;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        config = this.getConfig();
        instance = this;
    }

    @Override
    public void onDisable() {
        this.getName();
    }

    public static IndroMain getInstance() {
        return instance;
    }

    // config utils

    public FileConfiguration getSavedConfig() {
        return config;
    }

    public void reloadConfig() {
        config = this.getConfig();
    }

}
