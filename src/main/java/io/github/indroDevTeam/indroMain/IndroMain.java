package io.github.indroDevTeam.indroMain;

import io.github.indroDevTeam.indroMain.commands.*;
import io.github.indroDevTeam.indroMain.data.DataAPI;
import io.github.indroDevTeam.indroMain.data.SqliteDataApi;
import io.github.indroDevTeam.indroMain.events.EventOnPlayerJoin;
import me.kodysimpson.simpapi.menu.MenuManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class IndroMain extends JavaPlugin {
    private static IndroMain instance;
    private static DataAPI dataManager;

    @Override
    public void onEnable() {
        instance = this;
        try {
            dataManager = new SqliteDataApi(this);
        } catch (SQLException e) {
            e.printStackTrace();
            getPluginLoader().disablePlugin(this);
            return;
        }

        if (!getDataFolder().exists()) {
            if (getDataFolder().mkdir()) {
                getLogger().info("Config folder created successfully!");
                saveDefaultConfig();
            } else {
                getLogger().severe("Could not create config folder");
                getPluginLoader().disablePlugin(this);
                return;
            }
        }

        MenuManager.setup(getServer(), this);

        // load data
        loadCommands();
        loadEvents();
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
    }

    private void loadCommands() {
        this.getCommand("home").setExecutor(new CommandHome());
        this.getCommand("home").setTabCompleter(new CommandHome());

        this.getCommand("sethome").setExecutor(new CommandSetHome());

        this.getCommand("delhome").setExecutor(new CommandDelHome());
        this.getCommand("delhome").setTabCompleter(new CommandDelHome());

        this.getCommand("homes").setExecutor(new CommandHomes());

        this.getCommand("profile").setExecutor(new CommandProfile());
        this.getCommand("profile").setExecutor(new CommandProfile());
    }

    private void loadEvents() {
        this.getServer().getPluginManager().registerEvents(new EventOnPlayerJoin(), this);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Class-Type Methods
    ///////////////////////////////////////////////////////////////////////////

    public static void sendParsedMessage(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&1[&9IndroMain&1]&r " + message));
    }

    public static IndroMain getInstance() {
        return instance;
    }

    public static DataAPI getDataManager() {
        return dataManager;
    }
}