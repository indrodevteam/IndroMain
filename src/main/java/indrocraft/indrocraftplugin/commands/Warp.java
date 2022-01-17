package indrocraft.indrocraftplugin.commands;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.utils.ConfigUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class Warp implements TabExecutor {

    ConfigUtils c = new ConfigUtils(Main.getPlugin(Main.class), "warps.yml");
    FileConfiguration config = c.getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        boolean warpsEnabled = config.getBoolean("useWarp");
        Player player = (Player) sender;
        ChatColor red = ChatColor.RED;

        if (label.equalsIgnoreCase("toggleWarp")) {
            if (player.isOp()) {
                switch (args[0].toLowerCase(Locale.ROOT)) {
                    case "true":
                        config.set("useWarp", true);
                        c.saveConfig();
                        player.sendMessage(ChatColor.GREEN + "/warp command enabled!");
                        break;
                    case "false":
                        config.set("useWarp", false);
                        c.saveConfig();
                        player.sendMessage(ChatColor.GREEN + "/warp command disabled!");
                        break;
                    default:
                        player.sendMessage(red + "First argument must be 'true' or 'false'!");
                }
                return true;
            } else {
                player.sendMessage(red + "You do not have permission to do this!");
            }


        } else if (warpsEnabled) {
            if (args.length > 0) {

                String warpName = args[0];

                    List<String> setWarps = config.getStringList("locations");
                    switch (label.toLowerCase(Locale.ROOT)) {
                        case "setwarp":
                            if (player.isOp()) {
                                if (setWarps.contains(warpName)) {
                                    player.sendMessage(red + "This warp point already exists");
                                    return true;
                                } else {
                                    setWarps.add(warpName);
                                    config.set("locations", setWarps);
                                    c.saveConfig();

                                    String getWorld = player.getWorld().toString();
                                    getWorld = getWorld.substring(0, getWorld.length() - 1);
                                    String[] world = getWorld.split("\\=");

                                    //location and direction of player:
                                    double x = player.getLocation().getX();
                                    double y = player.getLocation().getY();
                                    double z = player.getLocation().getZ();
                                    float yaw = player.getLocation().getYaw();
                                    float pitch = player.getLocation().getPitch();

                                    //creates new row and fills in location data
                                    config.set("warps." + warpName + ".x", x);
                                    config.set("warps." + warpName + ".y", y);
                                    config.set("warps." + warpName + ".z", z);
                                    config.set("warps." + warpName + ".pitch", pitch);
                                    config.set("warps." + warpName + ".yaw", yaw);
                                    config.set("warps." + warpName + ".world", world[1]);
                                    c.saveConfig();

                                    player.sendMessage(ChatColor.BLUE + "Successfully set warp: " + ChatColor.GREEN + warpName);
                                }
                            } else {
                                player.sendMessage(red + "You do not have permission to do that!");
                            }
                            break;
                        case "delwarp":
                            if (player.isOp()) {
                                if (setWarps.contains(warpName)) {
                                    setWarps.remove(warpName);
                                    config.set("locations", setWarps);
                                    c.saveConfig();

                                    config.set("warps." + warpName, null);

                                    //main.sqlUtils.remove("warpID", warpName, table);
                                    player.sendMessage(ChatColor.BLUE + "Successfully removed warp: " + ChatColor.GREEN + warpName);
                                } else {
                                    player.sendMessage(red + "This warp point already exists");
                                    return true;
                                }
                            } else {
                                player.sendMessage(red + "You do not have permission to do that!");
                            }
                            break;
                    }
                if (label.equalsIgnoreCase("warp")) {
                    if (config.getStringList("locations").contains(args[0])) {
                        double x = config.getDouble("warps." + warpName + ".x");
                        double y = config.getDouble("warps." + warpName + ".y");
                        double z = config.getDouble("warps." + warpName + ".z");
                        float pitch = Float.parseFloat(Objects.requireNonNull(config.get("warps." + warpName + ".pitch")).toString());
                        float yaw = Float.parseFloat(Objects.requireNonNull(config.get("warps." + warpName + ".yaw")).toString());
                        String targetWorld = config.getString("warps." + warpName + ".world");

                        assert targetWorld != null;
                        World world = Bukkit.getWorld(targetWorld);
                        Location location = new Location(world, x, y, z, yaw, pitch);

                        Chunk chunk = player.getWorld().getChunkAt(location);
                        player.getWorld().loadChunk(chunk);

                        try {
                            player.teleport(location);
                            player.sendMessage(ChatColor.BLUE + "Teleporting to " + ChatColor.GREEN + warpName + ChatColor.BLUE + " now!");
                        } catch (IllegalArgumentException e) {
                            player.sendMessage(red + "Must have a valid name after the command /setwarp");
                            e.printStackTrace();
                            return true;
                        }
                    } else {
                        player.sendMessage(red + "The warp location: " + ChatColor.DARK_RED + args[0] + red + "does not exist! Please check your spelling and try again.");
                    }
                }
            } else {
                player.sendMessage(red + "Must have a location name after '/warp'!");
            }
        } else {
            player.sendMessage(red + "Please enable the warp command in the config file!");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] strings) {
        if (alias.equalsIgnoreCase("warp") || alias.equalsIgnoreCase("delwarp")) {
            List<String> setWarps = config.getStringList("locations");

            //create new array
            final List<String> completions = new ArrayList<>();
            //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
            StringUtil.copyPartialMatches(strings[0], setWarps, completions);
            //sort the list
            Collections.sort(completions);
            return completions;
        } else if (alias.equalsIgnoreCase("togglewarp")) {
            List<String> bool = new ArrayList<>();
            bool.add("true");
            bool.add("false");
            return bool;
        } else {
            return null;
        }
    }
}
