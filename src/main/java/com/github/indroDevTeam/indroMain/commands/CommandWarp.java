package com.github.indroDevTeam.indroMain.commands;

import com.github.indroDevTeam.indroMain.dataUtils.LanguageTags;
import com.github.indroDevTeam.indroMain.teleports.Point;
import com.github.indroDevTeam.indroMain.teleports.PointStorage;
import com.github.indroDevTeam.indroMain.teleports.PointUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class CommandWarp implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            switch (label.toLowerCase(Locale.ROOT)) {
                case "warp" -> {
                    Point point = PointStorage.findPoint("SERVER", args[0]);
                    if (args.length == 1 && point != null) {
                        player.sendMessage(LanguageTags.JUMP_LOADING.get());
                        PointUtils.warp(player, point);
                    } else {
                        player.sendMessage(LanguageTags.JUMP_FAILED.get());
                    }
                    return true;
                }
                case "setwarp" -> {
                    if (args.length == 1) {
                        if (player.isOp()) {
                            PointUtils.createWarp(args[0], player.getLocation());
                            player.sendMessage(LanguageTags.SET_WARP_SUCCESS.get());
                        } else {
                            player.sendMessage(LanguageTags.ERROR_PERMISSION.get());
                        }
                        return true;
                    }
                }
                case "delwarp" -> {
                    if (args.length == 1) {
                        if (player.isOp()) {
                            Point point = PointStorage.findPoint("SERVER", args[0]);
                            if (point != null) {
                                PointStorage.deletePoint(point.getPointName(), "SERVER");
                                player.sendMessage(LanguageTags.DEL_WARP_SUCCESS.get());
                            } else {
                                player.sendMessage(LanguageTags.ERROR_POINT_EXIST.get());
                            }
                        }
                        return true;
                    }
                }
                case "listwarps" -> {
                    LinkedList<String> points = new LinkedList<>();

                    for (Point point : PointStorage.findPointsWithOwner("SERVER")) {
                        String pointData = String.format("|| %s - Coordinates: %s, %s, %s",
                                point.getPointName(), Math.round(point.getX()), Math.round(point.getY()), Math.round(point.getZ()));
                        points.add(pointData);
                    }
                    if (points.isEmpty()) {
                        points.add("- WARP LIST EMPTY");
                    }
                    points.addFirst("++===================++");
                    points.add("++===================++");
                    for (String line : points) {
                        player.sendMessage(line);
                    }
                    return true;
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
        if (sender instanceof Player) {
            switch (alias.toLowerCase(Locale.ROOT)) {
                case "warp", "delwarp" -> {
                    if (args.length == 1) {
                        ArrayList<Point> userList = PointStorage.findPointsWithOwner("SERVER");
                        for (Point point:
                                userList) {
                            arguments.add(point.getPointName());
                        }
                    }
                }
                case "setwarp", "listwarps" -> {}
            }
        }
        return arguments;
    }
}
