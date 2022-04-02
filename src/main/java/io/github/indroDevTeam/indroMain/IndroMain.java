package io.github.indroDevTeam.indroMain;

import io.github.indroDevTeam.indroMain.commands.CommandInfo;
import io.github.indroDevTeam.indroMain.commands.CommandHome;
import io.github.indroDevTeam.indroMain.commands.rank.*;
import io.github.indroDevTeam.indroMain.commands.CommandWarp;
import io.github.indroDevTeam.indroMain.dataUtils.YamlUtils;
import io.github.indroDevTeam.indroMain.events.EventOnPlayerJoin;
import io.github.indroDevTeam.indroMain.ranks.Rank;
import io.github.indroDevTeam.indroMain.ranks.RankStorage;
import io.github.indroDevTeam.indroMain.tasks.TaskAutoSave;
import io.github.indroDevTeam.indroMain.tasks.TaskCheckRanks;
import io.github.indroDevTeam.indroMain.teleports.PointStorage;
import me.kodysimpson.simpapi.command.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
            RankStorage.loadRanks();
            PointStorage.loadPoints();
        } catch (IOException e) {
            e.printStackTrace();
        }

        YamlUtils yamlUtils = new YamlUtils("playerRankList");
        yamlUtils.createFile();
        yamlUtils.loadFromFile();
        config = this.getConfig();

        updatePlayerRankList();
        runTasks();
        registerCommands();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        YamlUtils yamlUtils = new YamlUtils("playerRankList");
        YamlConfiguration configuration = new YamlConfiguration();
        for (UUID uuid : playerRankList.keySet()) {
            configuration.set(uuid.toString(), playerRankList.get(uuid));
        }
        yamlUtils.saveFile(yamlUtils.getConfig());
    }

    public void registerCommands() {
        Bukkit.getPluginManager().registerEvents(new EventOnPlayerJoin(), this);
        Bukkit.getPluginCommand("home").setExecutor(new CommandHome());
        Bukkit.getPluginCommand("home").setTabCompleter(new CommandHome());
        Bukkit.getPluginCommand("warp").setExecutor(new CommandWarp());
        Bukkit.getPluginCommand("warp").setTabCompleter(new CommandWarp());
        Bukkit.getPluginCommand("info").setExecutor(new CommandInfo());

        try {
            CommandManager.createCoreCommand(this, "rank",
                    "Allows admins to create, modify, and mess around with ranks",
                    "/rank", null, CommandSetRank.class, CommandReloadRanks.class,
                    CommandVersion.class, CommandCreateRank.class, CommandRankInfo.class
            );
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void runTasks() {
        TaskCheckRanks.run();
        new TaskAutoSave(this).runTaskTimer(this, 0, 200);
    }

    public void updatePlayerRankList() {
        // update the player rank list
        YamlUtils yamlUtils = new YamlUtils("playerRankList");
        yamlUtils.loadFromFile();
        FileConfiguration file = YamlConfiguration.loadConfiguration(yamlUtils.getFile());

        List<String> stringList = file.getStringList("");
        for (String s : stringList) {
            UUID playerUUID = UUID.fromString(s);
            Rank rank = (Rank) file.get(s);

            playerRankList.put(playerUUID, rank);
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
