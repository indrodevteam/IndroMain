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

    ///////////////////////////////////////////////////////////////////////////
    // Class-Type Methods
    ///////////////////////////////////////////////////////////////////////////

    public static IndroMain getInstance() {
        return instance;
    }
}