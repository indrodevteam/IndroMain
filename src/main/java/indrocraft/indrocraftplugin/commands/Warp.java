package indrocraft.indrocraftplugin.commands;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.dataManager.ConfigTools;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Warp implements TabExecutor {

    private Main main = Main.getPlugin(Main.class);

    ConfigTools config = new ConfigTools(main, "warps.yml");

    public String table = config.getConfig().getString("databaseForTP") + "Warps";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length > 0) {
            Player player = (Player) sender;
            ChatColor red = ChatColor.RED;
            String warpName = args[0];

            if (player.isOp()) {
                List<String> setWarps = config.getConfig().getStringList("locations");
                switch (label.toLowerCase(Locale.ROOT)) {
                    case "setwarp":
                        if (setWarps.contains(warpName)) {
                            player.sendMessage(red + "This warp point already exists");
                            return true;
                        } else {
                            setWarps.add(warpName);
                            config.getConfig().set("locations", setWarps);
                            config.saveConfig();

                            main.sqlUtils.createRow("warpID", warpName, table);

                            String getWorld = player.getWorld().toString();
                            getWorld = getWorld.substring(0, getWorld.length() - 1);
                            String[] world = getWorld.split("\\=");

                            //location and direction of player:
                            Double x = player.getLocation().getX();
                            Double y = player.getLocation().getY();
                            Double z = player.getLocation().getZ();

                            //creates new row and fills in location data
                            main.sqlUtils.setData(x.toString(), "warpID", warpName, "x", table);
                            main.sqlUtils.setData(y.toString(), "warpID", warpName, "y", table);
                            main.sqlUtils.setData(z.toString(), "warpID", warpName, "z", table);
                            main.sqlUtils.setData(world[1], "warpID", warpName, "world", table);

                            player.sendMessage(ChatColor.BLUE + "Successfully set warp: " + ChatColor.GREEN + warpName);
                        }
                        break;
                    case "delwarp":
                        if (setWarps.contains(warpName)) {
                            setWarps.remove(warpName);
                            config.getConfig().set("locations", setWarps);
                            config.saveConfig();

                            main.sqlUtils.remove("warpID", warpName, table);
                            player.sendMessage(ChatColor.BLUE + "Successfully removed warp: " + ChatColor.GREEN + warpName);
                        } else {
                            player.sendMessage(red + "This warp point already exists");
                            player.sendMessage(red + "This warp point already exists");
                            return true;
                        }
                        break;
                    case "warp":
                        double x = main.sqlUtils.getDouble("x", "warpID", warpName, table);
                        double y = main.sqlUtils.getDouble("y", "warpID", warpName, table);
                        double z = main.sqlUtils.getDouble("z", "warpID", warpName, table);
                        String targetWorld = main.sqlUtils.getString("world", "warpID", warpName, table);

                        World world = Bukkit.getWorld(targetWorld);
                        Location location = new Location(world, x, y, z);

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
                        break;
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] strings) {
        List<String> setWarps = config.getConfig().getStringList("locations");

        //create new array
        final List<String> completions = new ArrayList<>();
        //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
        StringUtil.copyPartialMatches(strings[0], setWarps, completions);
        //sort the list
        Collections.sort(completions);
        return completions;
    }
}
