package io.github.indroDevTeam.indroMain.commands.warp;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.dataUtils.LanguageLoader;
import io.github.indroDevTeam.indroMain.teleports.PointStorage;
import io.github.indroDevTeam.indroMain.teleports.Point;
import io.github.indroDevTeam.indroMain.teleports.PointUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandWarp implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("You are missing a location, incorporeal being!");
        } else {
            if (label.equalsIgnoreCase("warp")) {
                Point point = PointStorage.findPoint(IndroMain.getInstance().getServer().getName(), args[0]);
                if (args.length == 1 && point != null) {
                    player.sendMessage(new LanguageLoader().get("plugin-title") + new LanguageLoader().get("jump-loading"));
                    PointUtils.warp(player, point);
                } else {
                    player.sendMessage(new LanguageLoader().get("plugin-title") + new LanguageLoader().get("jump-failed"));
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> arguments = new ArrayList<>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                ArrayList<Point> userList = PointStorage.findPointsWithOwner(IndroMain.getInstance().getServer().getName());
                for (Point point:
                        userList) {
                    arguments.add(point.getHomeName());
                }
            }
        }
        return arguments;
    }
}
