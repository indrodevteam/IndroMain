package io.github.indroDevTeam.indroMain.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager.Profession;
import org.jetbrains.annotations.NotNull;

import com.fasterxml.jackson.databind.type.PlaceholderForType;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.data.Point;

public class CommandHome implements TabExecutor {
    private IndroMain plugin;

    public CommandHome(IndroMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return false;

        Profile profile = plugin

        switch (label.toLowerCase(Locale.ROOT)) {
            case "home" -> {
                if (args.length == 1) {
                    Point point = 
                    player.sendMessage(// TODO: Replace this with propeer context.JUMP_FAILED.get());
                    return true;
                }
            }
            case "delhome" -> {
                if (args.length == 1) {
                    Point point = PointStorage.findPoint(player.getUniqueId().toString(), args[0]);
                    if (point != null) {
                        PointStorage.deletePoint(point.getPointName(), player.getUniqueId().toString());
                        player.sendMessage(// TODO: Replace this with propeer context.DEL_HOME_SUCCESS.get()); 
                    } else {
                        player.sendMessage(// TODO: Replace this with propeer context.ERROR_POINT_EXIST.get());
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
                        player.sendMessage(// TODO: Replace this with propeer context.SET_HOME_SUCCESS.get());
                    }
                    return true;
                }
            }
        }
        player.sendMessage(// TODO: Replace this with propeer context.ERROR_SYNTAX.get());
    
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
