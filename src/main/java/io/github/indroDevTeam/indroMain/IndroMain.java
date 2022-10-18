package io.github.indroDevTeam.indroMain;

import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

public class IndroMain extends JavaPlugin {
    private static IndroMain instance;
    private static ProfileAPI profileAPI;

    @Override
    public void onEnable() {
        instance = this;
        try {
            profileAPI = new ProfileAPI();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            this.getServer().getPluginManager().disablePlugin(this);
        }

        // load data
        loadCommands();
        loadEvents();
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
    }

    private void loadCommands() {
        this.getCommand("");
    }

    private void loadEvents() {

    }

    ///////////////////////////////////////////////////////////////////////////
    // Class-Type Methods
    ///////////////////////////////////////////////////////////////////////////

    public static IndroMain getInstance() {
        return instance;
    }

    public static ProfileAPI getProfileAPI() {
        return profileAPI;
    }
}