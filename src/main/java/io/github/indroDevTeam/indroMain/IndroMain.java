package io.github.indroDevTeam.indroMain;

import org.bukkit.plugin.java.JavaPlugin;

public class IndroMain extends JavaPlugin {
    private static IndroMain instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
    }

    private void loadCommands() {
        this.getCommand("")
    }

    private void loadEvents() {

    }

    ///////////////////////////////////////////////////////////////////////////
    // Class-Type Methods
    ///////////////////////////////////////////////////////////////////////////

    public static IndroMain getInstance() {
        return instance;
    }
}