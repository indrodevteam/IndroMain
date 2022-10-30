package io.github.indroDevTeam.indroMain;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import io.github.indroDevTeam.indroMain.commands.CommandHome;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.kodysimpson.simpapi.menu.MenuManager;

public class IndroMain extends JavaPlugin {
    private static IndroMain instance;
    private static DataController dataController;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        try {
            dataController = new DataController();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    }

    private void loadEvents() {

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

    public static DataController getDataController() {
        return dataController;
    }
}