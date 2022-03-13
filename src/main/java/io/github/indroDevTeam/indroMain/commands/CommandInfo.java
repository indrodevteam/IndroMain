package io.github.indroDevTeam.indroMain.commands;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.dataUtils.LanguageLoader;
import io.github.indroDevTeam.indroMain.ranks.Rank;
import io.github.indroDevTeam.indroMain.teleports.PointStorage;
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
            sender.sendMessage(new LanguageLoader().get("plugin-title") + new LanguageLoader().get("error-player-only"));
        } else {
            if (label.equalsIgnoreCase("info")) {
                Rank rank = IndroMain.getPlayerRankList().get(player.getUniqueId());
                int seconds = Math.toIntExact(player.getWorld().getTime() / 72);
                LocalTime time = LocalTime.ofSecondOfDay(seconds);
                String[] playerData = new String[]{
                        ChatColor.AQUA + "-------------------------",
                        ChatColor.AQUA + "| Hello there, " + player.getName(),
                        ChatColor.AQUA + "| There are currently " + ChatColor.RED + IndroMain.getInstance().getServer().getOnlinePlayers().size() + ChatColor.AQUA + " player/s online.",
                        ChatColor.AQUA + "| Current Rank: " + rank.getRankName(),
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
