package io.github.indroDevTeam.indroMain;

import io.github.indroDevTeam.indroMain.commands.*;
import io.github.indroDevTeam.indroMain.commands.ranks.CommandRankInfo;
import io.github.indroDevTeam.indroMain.data.DataAPI;
import io.github.indroDevTeam.indroMain.data.SqliteDataApi;
import io.github.indroDevTeam.indroMain.events.EventOnAdvancement;
import io.github.indroDevTeam.indroMain.events.EventOnPlayerJoin;
import io.github.indroDevTeam.indroMain.model.Rank;
import io.github.indroDevTeam.indroMain.utils.Cooldowns;
import me.kodysimpson.simpapi.command.CommandManager;
import me.kodysimpson.simpapi.menu.MenuManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class IndroMain extends JavaPlugin {
    private static IndroMain instance;
    private static DataAPI dataManager;
    private static Cooldowns cooldowns;

    @Override
    public void onEnable() {
        instance = this;
        cooldowns = new Cooldowns();
        if (!getDataFolder().exists()) {
            if (getDataFolder().mkdir()) {
                getLogger().info("Config folder created successfully!");
                //saveDefaultConfig();
            } else {
                getLogger().severe("Could not create config folder");
                getPluginLoader().disablePlugin(this);
                return;
            }
        }

        try {
            dataManager = new SqliteDataApi(this);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            getPluginLoader().disablePlugin(this);
            return;
        }

        testDefaultRank();

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

        try {
            CommandManager.createCoreCommand(
                    this,
                    "rank",
                    "Handles the rank section of the plugin",
                    "/<command> <args>",
                    null,
                    CommandRankInfo.class
            );
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadEvents() {
        this.getServer().getPluginManager().registerEvents(new EventOnPlayerJoin(), this);
        this.getServer().getPluginManager().registerEvents(new EventOnAdvancement(), this);
    }

    private void testDefaultRank() {
        if (dataManager.getAllRanks().isEmpty()) {
            dataManager.createRank(new Rank("default", "{TEST}", "[TEST]", new ArrayList<>(), Arrays.asList("story/mine_stone", "story/upgrade_tools"), 2, 10, 15, 250, true));
        }
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

    public static Cooldowns getCooldowns() {
        return cooldowns;
    }
}