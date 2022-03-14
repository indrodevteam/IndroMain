package io.github.indroDevTeam.indroMain.commands.home;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.dataUtils.LanguageLoader;
import io.github.indroDevTeam.indroMain.ranks.Rank;
import io.github.indroDevTeam.indroMain.teleports.Point;
import io.github.indroDevTeam.indroMain.teleports.PointStorage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandHomeList implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(new LanguageLoader().get("plugin-title") + new LanguageLoader().get("error-player-only"));
        } else {
            if (label.equalsIgnoreCase("homelist")) {
                Rank rank = IndroMain.getPlayerRankList().get(player.getUniqueId());
                int currentHomes = PointStorage.findPointsWithOwner(player.getUniqueId().toString()).size();
                int maxHomes = rank.getMaxHomes();
                String[] playerData = new String[] {
                        ChatColor.AQUA + "======================",
                        ChatColor.AQUA + "| You are rank " + rank.getRankName() + " and have " + currentHomes + "/" + maxHomes + " homes."
                };
                List<String> stringList = new ArrayList<>(List.of(playerData));
                for (Point point : PointStorage.findPointsWithOwner(player.getUniqueId().toString())) {
                    stringList.add(ChatColor.AQUA + "| - " + point.getHomeName());
                }
                stringList.add(ChatColor.AQUA + "======================");

                for (String string: stringList) {
                    player.sendMessage(string);
                }
            }
        }
        return true;
    }
}
