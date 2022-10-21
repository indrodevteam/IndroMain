package io.github.indrodevteam.indroMain.commands;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.indrodevteam.indroMain.IndroMain;
import io.github.indrodevteam.indroMain.data.Point;
import io.github.indrodevteam.indroMain.data.Profile;
import me.kodysimpson.simpapi.command.SubCommand;
import net.md_5.bungee.api.ChatColor;

public class CoreCommandHome extends SubCommand {

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "The /home command allows you to teleport to a specific home.";
    }

    @Override
    public String getName() {
        return "home";
    }

    @Override
    public List<String> getSubcommandArguments(Player sender, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (sender instanceof Player player) {
            Profile profile = IndroMain.getProfileAPI().findProfile(player.getUniqueId());

            switch (args[0].toLowerCase(Locale.ROOT)) {
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

    @Override
    public String getSyntax() {
        return "/home <command> [pointName]";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

        Profile profile = IndroMain.getProfileAPI().findProfile(player.getUniqueId());

        if (args.length < 1) return;

        if (args[0].equals("del")) {
            if (args.length == 2) {
                Point point = profile.getPoint(args[1]);

                if (point == null) {
                    IndroMain.sendParsedMessage(player, ChatColor.RED + "Point does not exist!");
                    return;
                }
                
                Point removedPoint = null;
                for (Point p: profile.getPoints()) {
                    if (p.getName().equals(point.getName()))  {
                        removedPoint = p;
                    }
                }

                if (removedPoint == null) {
                    IndroMain.sendParsedMessage(player, ChatColor.RED + "Point does not exist!");
                    return;
                }

                profile.getPoints().remove(removedPoint);
                IndroMain.sendParsedMessage(player, ChatColor.YELLOW + point.getName() + " was successfully removed!");
            }
            return;
        }

        if (args[0].equals("list")) {
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
            return;
        }

        if (args[0].equals("set")) {
            if (args.length == 2) {
                if (profile.getPoints().size() >= profile.getWarpCap()) {
                    IndroMain.sendParsedMessage(player, ChatColor.YELLOW + "You have too many warps saved, delete one to add another!");
                    return;
                }

                profile.getPoints().add(new Point(args[1], player.getLocation()));
                if (profile.getPoint(args[1]) == null) {
                    IndroMain.sendParsedMessage(player, ChatColor.RED + "The point couldn't be saved!");
                    return;   
                }

                IndroMain.sendParsedMessage(player, ChatColor.AQUA + "The point was successfully saved!");
            }
            return;
        }

        // teleport to homes...

        if (args[0].equals("warp")) {
            if (args.length >= 2) {
                Point point = profile.getPoint(args[1]);

                if (point == null) {
                    IndroMain.sendParsedMessage(player, ChatColor.RED + "Point could not be found!");
                    return;
                }
        
                if (point.getDistance(player) >= profile.getMaxDistance()) {
                    IndroMain.sendParsedMessage(player, ChatColor.RED + "You're too far away to teleport there!");
                    return;
                }
        
                if (!profile.isCrossWorldPermitted() && player.getLocation().getWorld().getName().equals(point.getLocation().getWorld().getName())) {
                    IndroMain.sendParsedMessage(player, ChatColor.RED + "This point is outside your dimension...");
                    return;
                }
        
                // teleport is cleared if the teleport is there...
                profile.warp(player, point);
                return;
            }
        }
    }
}
