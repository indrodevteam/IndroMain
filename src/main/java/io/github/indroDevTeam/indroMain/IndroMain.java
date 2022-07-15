package io.github.indroDevTeam.indroMain;

import io.github.indroDevTeam.indroMain.managers.SettingsManager;
import org.bukkit.plugin.java.JavaPlugin;

public class IndroMain extends JavaPlugin {
    private static IndroMain instance;
    private SettingsManager settingsManager;

    @Override
    public void onEnable() {
        instance = this;
        settingsManager = new SettingsManager();


        getLogger().info("Multithreading: " + IndroMain.getInstance().getSettingsManager().getUseThreads());
    }

    @Override
    public void onDisable() {

    }

    ///////////////////////////////////////////////////////////////////////////
    // Class-Type Methods
    ///////////////////////////////////////////////////////////////////////////

    public static IndroMain getInstance() {
        return instance;
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}