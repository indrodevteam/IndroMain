package io.github.indroDevTeam.indroMain.teleports;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.ranks.Rank;
import io.github.indroDevTeam.indroMain.ranks.UserRanks;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PointUtils {
    public static boolean createHome(String homeName, Player player, Location location) {
        Rank playerRank = UserRanks.getRank(player);
        List<Point> playerPoints = PointStorage.findPointsWithOwner(player.getUniqueId());
        List<String> playerHomeNames = new ArrayList<>();
        for (Point point :
                playerPoints) {
            playerHomeNames.add(point.getPointName());
        }
        int playerMaxHomes = playerRank.getMaxHomes();
        ArrayList<Point> points = PointStorage.findPointsWithOwner(player.getUniqueId());

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
            PointStorage.createPoint(PointType.PRIVATE_HOME, homeName, player.getUniqueId().toString(), location);
            return true;
        }
    }

    public static void createWarp(String warpName, Location location) {
        String serverName = "SERVER";
        PointStorage.createPoint(PointType.PUBLIC_WARP, warpName, serverName, location);
    }

    public static void warp(Player player, Point point) { // assuming owner is an uuid
        if (point.getOwner().equalsIgnoreCase(player.getUniqueId().toString()) || point.getOwner().equals("SERVER")) {
            // start a task that counts the time before firing...
            point.warp(player);
        }
    }

}
