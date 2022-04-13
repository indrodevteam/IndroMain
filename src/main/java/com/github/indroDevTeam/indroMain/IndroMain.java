package com.github.indroDevTeam.indroMain;

import com.github.indroDevTeam.indroMain.commands.CommandHome;
import com.github.indroDevTeam.indroMain.commands.CommandInfo;
import com.github.indroDevTeam.indroMain.commands.rank.CommandListRanks;
import com.github.indroDevTeam.indroMain.commands.rank.CommandRankInfo;
import com.github.indroDevTeam.indroMain.commands.rank.CommandRankPromote;
import com.github.indroDevTeam.indroMain.commands.rank.CommandReloadRanks;
import com.github.indroDevTeam.indroMain.commands.rank.CommandSetNameColour;
import com.github.indroDevTeam.indroMain.commands.rank.CommandSetRank;
import com.github.indroDevTeam.indroMain.commands.rank.CommandVersion;
import com.github.indroDevTeam.indroMain.events.EventOnPlayerJoin;
import com.github.indroDevTeam.indroMain.ranks.RankStorage;
import com.github.indroDevTeam.indroMain.ranks.UserRanks;
import com.github.indroDevTeam.indroMain.tasks.TaskAutoSave;
import com.github.indroDevTeam.indroMain.teleports.PointStorage;
import com.github.indroDevTeam.indroMain.commands.CommandWarp;
import com.github.indroDevTeam.indroMain.events.EventOnAdvancement;
import com.github.indroDevTeam.indroMain.tasks.TaskCheckRanks;
import me.kodysimpson.simpapi.command.CommandManager;
import me.kodysimpson.simpapi.menu.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class IndroMain extends JavaPlugin {
    private FileConfiguration config;
    private static IndroMain instance;

    public static HashMap<UUID, Integer> warping = new HashMap<>();

    public static IndroMain getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        try {
            RankStorage.loadRanks();
            PointStorage.loadPoints();
            UserRanks.loadNameColor();
            UserRanks.loadUserRanks();
        } catch (IOException e) {
            e.printStackTrace();
        }
        config = this.getConfig();

        UserRanks.loadUserRanks();
        runTasks();
        registerCommands();

        //Setup and register the MenuManager. It will take care of the annoying parts.
        MenuManager.setup(getServer(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        UserRanks.saveUserRanks();
        try {
            RankStorage.saveRanks();
            PointStorage.savePoints();
            UserRanks.saveNameColor();
            UserRanks.saveUserRanks();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            String format = player.getName();
            player.setDisplayName(format);
            player.setPlayerListName(format);
        }
    }

    public void registerCommands() {
        Bukkit.getPluginManager().registerEvents(new EventOnPlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new EventOnAdvancement(), this);
        Bukkit.getPluginCommand("home").setExecutor(new CommandHome());
        Bukkit.getPluginCommand("home").setTabCompleter(new CommandHome());
        Bukkit.getPluginCommand("warp").setExecutor(new CommandWarp());
        Bukkit.getPluginCommand("warp").setTabCompleter(new CommandWarp());
        Bukkit.getPluginCommand("info").setExecutor(new CommandInfo());

        try {
            CommandManager.createCoreCommand(this, "rank",
                    "Allows admins to create, modify, and mess around with ranks",
                    "/rank", null,
                    //CommandCreateRank.class,
                    CommandListRanks.class,
                    CommandRankInfo.class,
                    CommandRankPromote.class,
                    CommandReloadRanks.class,
                    CommandSetRank.class,
                    CommandSetNameColour.class,
                    CommandVersion.class
            );
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void runTasks() {
        TaskCheckRanks.run();
        new TaskAutoSave(this).runTaskTimer(this, 0, 2400);
    }

    // config utils
    public FileConfiguration getSavedConfig() {
        return config;
    }

    public void updateConfig() {
        config = this.getConfig();
    }
}
