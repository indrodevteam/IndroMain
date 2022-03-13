package io.github.indroDevTeam.indroMain.commands.home;

import io.github.indroDevTeam.indroMain.dataUtils.LanguageLoader;
import io.github.indroDevTeam.indroMain.teleports.PointStorage;
import io.github.indroDevTeam.indroMain.teleports.PointUtils;
import io.github.indroDevTeam.indroMain.teleports.Point;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandHome implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(new LanguageLoader().get("plugin-title") + new LanguageLoader().get("error-player-only"));
        } else {
            if (label.equalsIgnoreCase("home") && args.length == 1) {
                Point point = PointStorage.findPoint(player.getUniqueId().toString(), args[0]);
                if (point != null) {
                    player.sendMessage(new LanguageLoader().get("plugin-title") + new LanguageLoader().get("jump-loading"));
                    PointUtils.warp(player, point);
                } else {
                    player.sendMessage(new LanguageLoader().get("plugin-title") + new LanguageLoader().get("jump-failed"));
                }
            } else {
                player.sendMessage(new LanguageLoader().get("plugin-title") + new LanguageLoader().get("error-syntax"));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> arguments = new ArrayList<>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                ArrayList<Point> userList = PointStorage.findPointsWithOwner(player.getUniqueId().toString());
                for (Point point:
                     userList) {
                    arguments.add(point.getHomeName());
                }
            }
        }
        return arguments;
    }
}
