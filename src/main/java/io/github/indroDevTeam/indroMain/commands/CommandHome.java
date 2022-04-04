package io.github.indroDevTeam.indroMain.commands;

import io.github.indroDevTeam.indroMain.dataUtils.LanguageTags;
import io.github.indroDevTeam.indroMain.teleports.PointStorage;
import io.github.indroDevTeam.indroMain.teleports.PointUtils;
import io.github.indroDevTeam.indroMain.teleports.Point;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class CommandHome implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            switch (label.toLowerCase(Locale.ROOT)) {
                case "home" -> {
                    if (args.length == 1) {
                        Point point = PointStorage.findPoint(player.getUniqueId().toString(), args[0]);
                        if (point != null) {
                            player.sendMessage(LanguageTags.JUMP_LOADING.get());
                            PointUtils.warp(player, point);
                            return true;
                        }
                        player.sendMessage(LanguageTags.JUMP_FAILED.get());
                        return true;
                    }
                }
                case "delhome" -> {
                    if (args.length == 1) {
                        Point point = PointStorage.findPoint(player.getUniqueId().toString(), args[0]);
                        if (point != null) {
                            PointStorage.deletePoint(point.getPointName(), player.getUniqueId().toString());
                            player.sendMessage(LanguageTags.DEL_HOME_SUCCESS.get());
                        } else {
                            player.sendMessage(LanguageTags.ERROR_POINT_EXIST.get());
                        }
                        return true;
                    }
                }
                case "listhomes" -> {
                    LinkedList<String> points = new LinkedList<>();
                    for (Point point: PointStorage.findPointsWithOwner(player.getUniqueId().toString())) {
                        String pointData = String.format("|| %s - Coordinates: %s, %s, %s",
                                point.getPointName(), Math.round(point.getX()), Math.round(point.getY()), Math.round(point.getZ()));
                        points.add(pointData);
                    }
                    if (points.isEmpty()) {
                        points.add("- HOME LIST EMPTY");
                    }
                    points.addFirst("++===================++");
                    points.add("++===================++");
                    for (String line: points) {
                        player.sendMessage(line);
                    }
                    return true;
                }
                case "sethome" -> {
                    if (args.length == 1) {
                        boolean result = PointUtils.createHome(args[0], player, player.getLocation());
                        if (result) {
                            player.sendMessage(LanguageTags.SET_HOME_SUCCESS.get());
                        }
                        return true;
                    }
                }
            }
            player.sendMessage(LanguageTags.ERROR_SYNTAX.get());
        } else {
            sender.sendMessage(LanguageTags.ERROR_PLAYER_ONLY.get());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> arguments = new ArrayList<>();
        if (sender instanceof Player player) {
            switch (alias.toLowerCase(Locale.ROOT)) {
                case "home", "delhome" -> {
                    if (args.length == 1) {
                        ArrayList<Point> userList = PointStorage.findPointsWithOwner(player.getUniqueId());
                        for (Point point:
                                userList) {
                            arguments.add(point.getPointName());
                        }
                    }
                }
                case "listhome", "sethome" -> {}
            }
        }
        return arguments;
    }
}
