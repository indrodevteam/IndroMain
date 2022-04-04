package io.github.indroDevTeam.indroMain.ranks;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.dataUtils.YamlUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

public class UserRanks {
    private static final HashMap<UUID, String> playerRankList = new HashMap<>();
    private static final HashMap<UUID, String> playerNameColor = new HashMap<>();

    public static void loadUserRanks() {
        File file = new File(IndroMain.getInstance().getDataFolder().getAbsolutePath() + File.separator + "playerRanks.yml");
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            saveUserRanks();
        }
        HashMap<String, Object> values = (HashMap<String, Object>) configuration.getValues(false);
        for (String s : values.keySet()) {
            UUID key = UUID.fromString(s);
            String value = (String) values.get(s);
            playerRankList.put(key, value);
        }
    }

    public static void saveUserRanks() {
        File file = new File(IndroMain.getInstance().getDataFolder().getAbsolutePath() + File.separator + "playerRanks.yml");
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        for (UUID playerID: playerRankList.keySet()) {
            configuration.set(String.valueOf(playerID), playerRankList.get(playerID));
        }
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updatePlayerRankList() {
        // update the player rank list
        YamlUtils yamlUtils = new YamlUtils("playerRankList");
        yamlUtils.loadFromFile();
        FileConfiguration file = YamlConfiguration.loadConfiguration(yamlUtils.getFile());

        List<String> stringList = file.getStringList("");
        for (String s : stringList) {
            UUID playerUUID = UUID.fromString(s);
            Rank rank = (Rank) file.get(s);

            playerRankList.put(playerUUID, rank.getRankTag());
        }
    }

    @NotNull
    public static Rank getRank(Player player) {
        Rank rank = RankStorage.readRank(playerRankList.get(player.getUniqueId()));
        if (rank == null) {
            rank = RankStorage.readRank("DEFAULT");
            assert rank != null;
            playerRankList.put(player.getUniqueId(), "DEFAULT");
        }
        return rank;
    }

    public static void setRank(Player player, Rank rank) {
        playerRankList.replace(player.getUniqueId(), rank.getRankTag());
    }

    public static void loadNameColor() {
        File file = new File(IndroMain.getInstance().getDataFolder().getAbsolutePath() + File.separator + "playerNameColor.yml");
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            saveUserRanks();
        }
        HashMap<String, Object> values = (HashMap<String, Object>) configuration.getValues(false);
        for (String s : values.keySet()) {
            UUID key = UUID.fromString(s);
            String value = (String) values.get(s);
            playerNameColor.put(key, value);
        }
    }

    public static void saveNameColor() {
        File file = new File(IndroMain.getInstance().getDataFolder().getAbsolutePath() + File.separator + "playerNameColor.yml");
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        for (UUID playerID: playerNameColor.keySet()) {
            configuration.set(String.valueOf(playerID), playerNameColor.get(playerID));
        }
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    public static ChatColor getChatColor(Player player) {
        return readColour(playerNameColor.putIfAbsent(player.getUniqueId(), "WHITE"));
    }

    public static void setChatColor(Player player, String color) {
        playerNameColor.replace(player.getUniqueId(), color);
    }

    @NotNull
    public static ChatColor readColour(String color) {
        if (color == null) {
            return ChatColor.WHITE;
        }
        switch (color.toLowerCase()) {
            case "dark_red":
                return ChatColor.DARK_RED;
            case "red":
                return ChatColor.RED;
            case "gold":
                return ChatColor.GOLD;
            case "yellow":
                return ChatColor.YELLOW;
            case "dark_green":
                return ChatColor.DARK_GREEN;
            case "green":
                return ChatColor.GREEN;
            case "aqua":
                return ChatColor.AQUA;
            case "dark_aqua":
                return ChatColor.DARK_AQUA;
            case "dark_blue":
                return ChatColor.DARK_BLUE;
            case "blue":
                return ChatColor.BLUE;
            case "light_purple":
                return ChatColor.LIGHT_PURPLE;
            case "dark_purple":
                return ChatColor.DARK_PURPLE;
            case "white":
                return ChatColor.WHITE;
            case "gray":
                return ChatColor.GRAY;
            case "dark_gray":
                return ChatColor.DARK_GRAY;
            case "black":
                return ChatColor.BLACK;
            default:
                Bukkit.getLogger().warning("'" + color + "' is an invalid colour! try reloading the plugin.");
                Bukkit.getLogger().warning("Defaulting to white!");
                return ChatColor.WHITE;
        }
    }
    public static ChatColor readColour(Colours color) {
        return readColour(color.toString());
    }

    public enum Colours {
        DARK_RED,
        RED,
        GOLD,
        YELLOW,
        DARK_GREEN,
        GREEN,
        AQUA,
        DARK_AQUA,
        DARK_BLUE,
        BLUE,
        LIGHT_PURPLE,
        DARK_PURPLE,
        WHITE,
        GRAY,
        DARK_GRAY,
        BLACK;
    }
}
