package io.github.indrodevteam.indroMain.commands;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import io.github.indrodevteam.indroMain.IndroMain;
import io.github.indrodevteam.indroMain.data.Point;
import io.github.indrodevteam.indroMain.data.Profile;
import io.github.indrodevteam.indroMain.menus.ProfileMenu;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.MenuManager;

public class CommandHome implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return false;

        Profile profile = IndroMain.getProfileAPI().findProfile(player.getUniqueId());
        switch (label.toLowerCase(Locale.ROOT)) {
            case "home" -> { // warps you to a home
                if (args.length == 1) {
                    Point point = profile.getPoint(args[0]);

                    if (point == null) {
                        IndroMain.sendParsedMessage(player, ChatColor.RED + "Point could not be found!");
                        return true;
                    }

                    if (point.getDistance(player) >= profile.getMaxDistance()) {
                        IndroMain.sendParsedMessage(player, ChatColor.RED + "You're too far away to teleport there!");
                        return true;
                    }

                    if (!profile.isCrossWorldPermitted() && player.getLocation().getWorld().getName().equals(point.getLocation().getWorld().getName())) {
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
                    Point point = profile.getPoint(args[0]);

                    if (point == null) {
                        IndroMain.sendParsedMessage(player, ChatColor.RED + "Point does not exist!");
                        return true;
                    }

                    Point removedPoint = null;
                    for (Point p: profile.getPoints()) {
                        if (p.getName().equals(point.getName()))  {
                            removedPoint = p;
                        }
                    }

                    if (removedPoint == null) {
                        IndroMain.sendParsedMessage(player, ChatColor.RED + "Point does not exist!");
                    }

                    profile.getPoints().remove(removedPoint);
                    IndroMain.sendParsedMessage(player, ChatColor.YELLOW + point.getName() + " was successfully removed!");
                    return true;
                }
            }
            case "listhomes" -> {
                LinkedList<String> points = new LinkedList<>();
                points.add(ChatColor.BLUE + "+=======HOMES=======+");
                if (profile.getPoints().isEmpty()) {
                    points.add(ChatColor.BLUE + "||  HOME LIST EMPTY  ||");
                } else {
                    for (Point point : profile.getPoints()) {
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
                    if (profile.getPoints().size() >= profile.getWarpCap()) {
                        IndroMain.sendParsedMessage(player, ChatColor.YELLOW + "You have too many warps saved, delete one to add another!");
                        return true;
                    }

                    for (Point point: profile.getPoints()) {
                        if (point.getName().equals(args[0])) {
                            IndroMain.sendParsedMessage(player, ChatColor.YELLOW + "This point already exists! Pick a different name, or delete that point.");
                            return true;
                        }
                    }

                    profile.getPoints().add(new Point(args[0], player.getLocation()));
                    if (profile.getPoint(args[0]) == null) {
                        IndroMain.sendParsedMessage(player, ChatColor.RED + "The point couldn't be saved!");
                        return true;   
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
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> arguments = new ArrayList<>();
        if (sender instanceof Player player) {
            Profile profile = IndroMain.getProfileAPI().findProfile(player.getUniqueId());

            switch (alias.toLowerCase(Locale.ROOT)) {
                case "home", "delhome" -> {
                    if (args.length == 1) {
                        List<Point> userList = profile.getPoints();
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