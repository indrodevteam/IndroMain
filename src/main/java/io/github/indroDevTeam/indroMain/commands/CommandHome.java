package io.github.indroDevTeam.indroMain.commands;

import java.sql.SQLException;
import java.util.*;

import io.github.indroDevTeam.indroMain.model.DaoPoint;
import io.github.indroDevTeam.indroMain.model.DaoProfile;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.model.Point;
import io.github.indroDevTeam.indroMain.model.Profile;
import io.github.indroDevTeam.indroMain.menus.ProfileMenu;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.MenuManager;

public class CommandHome implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return false;
        Profile profile;
        try {
            if (DaoProfile.getInstance().find(player.getUniqueId()).isPresent()) {
                profile = DaoProfile.getInstance().find(player.getUniqueId()).get();
            } else {
                throw new SQLException("Null Value Exception");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        switch (label.toLowerCase(Locale.ROOT)) {
            case "home" -> { // warps you to a home
                if (args.length == 1) {
                    Point point;

                    try {
                        if (DaoPoint.getInstance().find(player.getUniqueId(), args[0]).isPresent()) {
                            point = DaoPoint.getInstance().find(player.getUniqueId(), args[0]).get();
                        } else {
                            throw new SQLException("Null Value Exception");
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    if (point.getDistance(player) >= profile.getRank().getMaxDistance()) {
                        IndroMain.sendParsedMessage(player, ChatColor.RED + "You're too far away to teleport there!");
                        return true;
                    }

                    if (!profile.getRank().isCrossWorldPermitted() && player.getLocation().getWorld().getName().equals(point.getLocation().getWorld().getName())) {
                        IndroMain.sendParsedMessage(player, ChatColor.RED + "This point is outside your dimension...");
                        return true;
                    }

                    // teleport is cleared if the teleport is there...
                    profile.warp(player, point);
                    return true;
                }
            }
            case "delhome" -> {
                if (args.length == 1) {
                    Point point;

                    try {
                        if (DaoPoint.getInstance().find(player.getUniqueId(), args[0]).isPresent()) {
                            point = DaoPoint.getInstance().find(player.getUniqueId(), args[0]).get();
                            DaoPoint.getInstance().delete(point);
                        } else {
                            throw new SQLException("Null Value Exception");
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    IndroMain.sendParsedMessage(player, ChatColor.YELLOW + point.getName() + " was successfully removed!");
                    return true;
                }
            }
            case "listhomes" -> {
                List<Point> pointList;
                try {
                    pointList = DaoPoint.getInstance().findAll(player.getUniqueId());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                LinkedList<String> points = new LinkedList<>();
                points.add(ChatColor.BLUE + "+=======HOMES=======+");
                if (pointList.isEmpty()) {
                    points.add(ChatColor.BLUE + "||  HOME LIST EMPTY  ||");
                } else {
                    for (Point point : pointList) {
                        String pointData = ChatColor.BLUE + "|| " + point.getName() + " - Distance: " + Math.round(point.getDistance(player)) + "m";
                        points.add(pointData);
                    }
                }
                points.add(ChatColor.BLUE + "+===================+");

                for (String line: points) {player.sendMessage(line);}
                return true;
            }
            case "sethome" -> {
                if (args.length == 1) {
                    List<Point> pointList;
                    try {
                        pointList = DaoPoint.getInstance().findAll(player.getUniqueId());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    if (pointList.size() >= profile.getRank().getWarpCap()) {
                        IndroMain.sendParsedMessage(player, ChatColor.YELLOW + "You have too many warps saved, delete one to add another!");
                        return true;
                    }

                    for (Point point: pointList) {
                        if (point.getName().equals(args[0])) {
                            IndroMain.sendParsedMessage(player, ChatColor.YELLOW + "This point already exists! Pick a different name, or delete that point.");
                            return true;
                        }
                    }

                    String id = UUID.randomUUID().toString();
                    try {
                        DaoPoint.getInstance().update(new Point(id, player.getUniqueId(), args[0], player.getLocation()));
                        if (DaoPoint.getInstance().find(UUID.fromString(id), args[0]).isEmpty()) {
                            IndroMain.sendParsedMessage(player, ChatColor.RED + "The point couldn't be saved!");
                            return true;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    IndroMain.sendParsedMessage(player, ChatColor.AQUA + "The point was successfully saved!");
                    return true;
                }
            }
            case "profile" -> {
                try {
                    MenuManager.openMenu(ProfileMenu.class, player);
                } catch (MenuManagerException | MenuManagerNotSetupException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> arguments = new ArrayList<>();
        if (sender instanceof Player player) {
            switch (alias.toLowerCase(Locale.ROOT)) {
                case "home", "delhome" -> {
                    if (args.length == 1) {
                        List<Point> userList;
                        try {
                            userList = DaoPoint.getInstance().findAll(player.getUniqueId());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        for (Point point: userList) {
                            arguments.add(point.getName());
                        }
                    }
                }
                case "listhome", "sethome" -> {}
            }
        }
        return arguments;
    } 
}