package indrocraft.indrocraftplugin.commands;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.dataManager.ConfigTools;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Warp implements TabExecutor {

    private Main main = Main.getPlugin(Main.class);

    ConfigTools config = new ConfigTools(main, "config.yml");

    public String table = config.getConfig().getString("databaseForTP") + "Warps";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        ChatColor red = ChatColor.RED;

        if (args.length >= 1) {
            String warpName = args[0];
            if (label.equalsIgnoreCase("setwarp")) {
                if (player.isOp()) {
                    if (!(main.sqlUtils.rowExists("warpID", warpName, table))) {

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
                    } else {
                        player.sendMessage(red + "You already have a location set with this name please delete it first!");
                        return true;
                    }
                } else {
                    player.sendMessage(red + "You do not have permission to do that");
                }
            } else if (label.equalsIgnoreCase("warp")) {
                if (args.length >= 1) {
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
                        return true;
                    }
                }
            }
        } else {
            player.sendMessage(red + "Must have a valid name after the command /setwarp");
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] strings) {
        if (strings.length == 1){
            Player player = (Player) sender;
            String rawHomes = getWarps();
            String[] homes = rawHomes.split(" ");
            List<String> arg1 = new ArrayList<>();
            int len = homes.length - 1;
            while (len > 0) {
                arg1.add(homes[len]);
                len--;
            }
            return arg1;
        }
        return null;
    }

    public String getWarps() {
        try {
            PreparedStatement ps = main.SQL.getConnection().prepareStatement("SELECT warpID FROM " + table);
            ResultSet rs = ps.executeQuery();
            String info = "";
            if (rs.next()) {
                info = rs.getString("warpID");
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
