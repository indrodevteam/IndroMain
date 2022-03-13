package io.github.indroDevTeam.indroMain.teleports;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.dataUtils.LanguageLoader;
import io.github.indroDevTeam.indroMain.ranks.Rank;
import io.github.indroDevTeam.indroMain.tasks.TaskWarpEffect;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PointUtils {
    public static boolean createHome(String homeName, Player player, Location location) {
        Rank playerRank = IndroMain.getPlayerRankList().get(player.getUniqueId());
        List<Point> playerPoints = PointStorage.findPointsWithOwner(player.getUniqueId().toString());
        List<String> playerHomeNames = new ArrayList<>();
        for (Point point :
                playerPoints) {
            playerHomeNames.add(point.getHomeName());
        }
        assert playerRank != null;
        int playerMaxHomes = playerRank.getMaxHomes();
        ArrayList<Point> points = PointStorage.findPointsWithOwner(player.getUniqueId().toString());

        // checker for point validity
        if (points.size() >= playerMaxHomes) {
            player.sendMessage(ChatColor.RED + "Warning: Home Capacity at Critical Levels!",
                    ChatColor.RED + "Delete a Home to save another one!");
            return false;
        } else if (playerHomeNames.contains(homeName)) {
            player.sendMessage(ChatColor.RED + "Warning: Home already exists!",
                    ChatColor.RED + "Delete that Home to use this name, or use a different name!");
            return false;
        } else {
            PointStorage.createPoint(PointType.PRIVATE_HOME.toString(), homeName, player.getUniqueId().toString(), location);
            return true;
        }
    }

    public static void createWarp(String warpName, Location location) {
        String serverName = IndroMain.getInstance().getServer().getName();
        PointStorage.createPoint(PointType.PUBLIC_WARP.toString(), warpName, serverName, location);
    }

    public static void warp(Player player, Point point) { // assuming owner is a uuid
        Location location = new Location(Bukkit.getServer().getWorld(point.getWorldName()), point.getX(), point.getY(), point.getZ(), point.getYaw(), point.getPitch());
        if (point.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
            // start a task that counts the time before firing...
            int id = new TaskWarpEffect(player).run();
            Bukkit.getScheduler().runTaskLater(IndroMain.getInstance(), () -> {
                Bukkit.getScheduler().cancelTask(id);
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
                player.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
                player.teleport(location);
                player.sendMessage(new LanguageLoader().get("plugin-title") + new LanguageLoader().get("jump-success"));
            }, 20L * 5);
        } else if (point.getOwner().equalsIgnoreCase(IndroMain.getInstance().getServer().getName())) {
            int id = new TaskWarpEffect(player).run();
            Bukkit.getScheduler().runTaskLater(IndroMain.getInstance(), () -> {
                Bukkit.getScheduler().cancelTask(id);
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
                player.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
                player.teleport(location);
                player.sendMessage(new LanguageLoader().get("plugin-title") + new LanguageLoader().get("jump-success"));
            }, 20L * 3);
        }
    }

}
