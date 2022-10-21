package io.github.indroDevTeam.indroMain;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.indroDevTeam.indroMain.commands.CommandHome;

public class IndroMain extends JavaPlugin {
    private static IndroMain instance;
    private static ProfileAPI profileAPI;

    @Override
    public void onEnable() {
        getLogger().info("Enabling Protocol");
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

        getLogger().info(profileAPI.list().toString());
    }

    @Override
    public void onDisable() {
        try {
            profileAPI.saveToResource();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public static ProfileAPI getProfileAPI() {
        return profileAPI;
    }
}