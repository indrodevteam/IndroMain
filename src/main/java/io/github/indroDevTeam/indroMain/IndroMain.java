package io.github.indroDevTeam.indroMain;

import io.github.indroDevTeam.indroMain.commands.*;
import io.github.indroDevTeam.indroMain.commands.home.CommandDelHome;
import io.github.indroDevTeam.indroMain.commands.home.CommandHome;
import io.github.indroDevTeam.indroMain.commands.home.CommandHomeList;
import io.github.indroDevTeam.indroMain.commands.home.CommandSetHome;
import io.github.indroDevTeam.indroMain.commands.rank.CommandReloadRanks;
import io.github.indroDevTeam.indroMain.commands.rank.CommandSetRank;
import io.github.indroDevTeam.indroMain.commands.rank.CommandVersion;
import io.github.indroDevTeam.indroMain.commands.warp.CommandDelWarp;
import io.github.indroDevTeam.indroMain.commands.warp.CommandSetWarp;
import io.github.indroDevTeam.indroMain.commands.warp.CommandWarp;
import io.github.indroDevTeam.indroMain.tasks.TaskCheckRanks;
import io.github.indroDevTeam.indroMain.teleports.PointStorage;
import io.github.indroDevTeam.indroMain.events.EventOnPlayerJoin;
import io.github.indroDevTeam.indroMain.ranks.Rank;
import io.github.indroDevTeam.indroMain.ranks.RankStorage;
import io.github.indroDevTeam.indroMain.tasks.TaskAutoSave;
import me.kodysimpson.simpapi.command.CommandManager;
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
    private static HashMap<UUID, Rank> playerRankList = new HashMap<>();

    public static IndroMain getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        try {
            RankStorage.loadFromResource();
            RankStorage.loadRanks();
            PointStorage.loadPoints();
        } catch (IOException e) {
            e.printStackTrace();
        }
        config = this.getConfig();

        checkInvalidDataPoints();

        runTasks();
        registerCommands();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    public void registerCommands() {
        Bukkit.getPluginManager().registerEvents(new EventOnPlayerJoin(), this);
        Bukkit.getPluginCommand("home").setExecutor(new CommandHome());
        Bukkit.getPluginCommand("home").setTabCompleter(new CommandHome());
        Bukkit.getPluginCommand("sethome").setExecutor(new CommandSetHome());
        Bukkit.getPluginCommand("sethome").setTabCompleter(new CommandSetHome());
        Bukkit.getPluginCommand("warp").setExecutor(new CommandWarp());
        Bukkit.getPluginCommand("warp").setTabCompleter(new CommandWarp());
        Bukkit.getPluginCommand("info").setExecutor(new CommandInfo());
        Bukkit.getPluginCommand("delhome").setExecutor(new CommandDelHome());
        Bukkit.getPluginCommand("delhome").setTabCompleter(new CommandDelHome());
        Bukkit.getPluginCommand("homelist").setExecutor(new CommandHomeList());
        Bukkit.getPluginCommand("setwarp").setExecutor(new CommandSetWarp());
        Bukkit.getPluginCommand("time").setExecutor(new CommandTime());
        Bukkit.getPluginCommand("time").setTabCompleter(new CommandTime());

        Bukkit.getPluginCommand("delwarp").setExecutor(new CommandDelWarp());
        Bukkit.getPluginCommand("delwarp").setTabCompleter(new CommandDelWarp());

        try {
            CommandManager.createCoreCommand(this, "rank",
                    "Allows admins to create, modify, and mess around with ranks",
                    "/rank", null, CommandSetRank.class, CommandReloadRanks.class,
                    CommandVersion.class
            );
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void runTasks() {
        new TaskCheckRanks().run();
        new TaskAutoSave(this).runTaskTimer(this, 0, 200);
    }

    public void checkInvalidDataPoints() {
        for (Player player : this.getServer().getOnlinePlayers()) {
            if (playerRankList.get(player.getUniqueId()) == null) {
                playerRankList.replace(player.getUniqueId(), RankStorage.readRank("DEFAULT"));
            }
        }
    }

    // user rank

    public static HashMap<UUID, Rank> getPlayerRankList() {
        return playerRankList;
    }

    public static void setPlayerRankList(HashMap<UUID, Rank> playerRankList) {
        IndroMain.playerRankList = playerRankList;
    }

    // config utils

    public FileConfiguration getSavedConfig() {
        return config;
    }

    public void updateConfig() {
        config = this.getConfig();
    }

    public Rank getRank(Player player) {
        return getPlayerRankList().get(player.getUniqueId());
    }
}
