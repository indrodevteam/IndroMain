package com.github.indroDevTeam.indroMain.commands;

import com.github.indroDevTeam.indroMain.ranks.Rank;
import com.github.indroDevTeam.indroMain.ranks.UserRanks;
import com.github.indroDevTeam.indroMain.IndroMain;
import com.github.indroDevTeam.indroMain.dataUtils.LanguageTags;
import com.github.indroDevTeam.indroMain.teleports.PointStorage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class CommandInfo implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(LanguageTags.ERROR_PLAYER_ONLY.get());
        } else {
            if (label.equalsIgnoreCase("info")) {
                Rank rank = UserRanks.getRank(player);
                int seconds = Math.toIntExact(player.getWorld().getTime() / 72);
                LocalTime time = LocalTime.ofSecondOfDay(seconds);
                String[] playerData = new String[]{
                        ChatColor.AQUA + "-------------------------",
                        ChatColor.AQUA + "| Hello there, " + player.getName(),
                        ChatColor.AQUA + "| There are currently " + ChatColor.RED + IndroMain.getInstance().getServer().getOnlinePlayers().size() + ChatColor.AQUA + " player/s online.",
                        ChatColor.AQUA + "| Current Rank: " + rank.getRankTag(),
                        ChatColor.AQUA + "| Home Status: " + PointStorage.findPointsWithOwner(player.getUniqueId().toString()).size() +"/"+ rank.getMaxHomes(),
                        ChatColor.AQUA + "| The time is: " + ChatColor.RED + time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                        ChatColor.AQUA + "-------------------------"
                };

                player.sendMessage(playerData);
            }
        }
        return true;
    }
}
