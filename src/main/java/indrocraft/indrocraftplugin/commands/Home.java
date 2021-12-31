package indrocraft.indrocraftplugin.commands;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.dataManager.ConfigTools;
import indrocraft.indrocraftplugin.utils.RankUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Home implements TabExecutor {

    private final Main main = Main.getPlugin(Main.class);

    ConfigTools config = new ConfigTools(Main.getPlugin(Main.class), "config.yml");
    public String databaseName = config.getConfig().getString("databaseForTP");

    private final RankUtils rankUtils = new RankUtils();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ChatColor red = ChatColor.RED;
        boolean homeEnabled = config.getConfig().getBoolean("homes");

        if (homeEnabled) {

            if (sender instanceof Player) {
                Player player = (Player) sender;
                String uuid = player.getUniqueId().toString();
                int getNumOfHomes = main.sqlUtils.getInt(databaseName + "num", "UUID", uuid,
                        "players");
                int numOfHomes = 0;
                int playerLevel = rankUtils.getLevel(player, main.sqlUtils);
                if (playerLevel == 0) {
                    numOfHomes = 1;
                } else if (playerLevel == 1) {
                    numOfHomes = 2;
                } else if (playerLevel == 2) {
                    numOfHomes = 3;
                } else if (playerLevel == 3) {
                    numOfHomes = 4;
                } else if (playerLevel == 4) {
                    numOfHomes = 5;
                } else if (playerLevel == 5) {
                    numOfHomes = 10;
                }
                if (main.sqlUtils.getString("nameColour", "UUID", uuid, "players").equalsIgnoreCase("gold")) {
                    numOfHomes = numOfHomes + 2;
                }
                //gets number of homes using: pRank.getLevel()
                if ("sethome".equalsIgnoreCase(label)) {
                    if (getNumOfHomes < numOfHomes) {
                        if (args.length > 0) {
                            String homeID = args[0];
                            String homesList = main.sqlUtils.getString(databaseName, "UUID", uuid,
                                    "players");
                            if (!(homesList.contains(" " + homeID + " "))) {
                                player.sendMessage(ChatColor.BLUE + "Successfully set home: " + ChatColor.GREEN +
                                        homeID);
                                int newNum = getNumOfHomes + 1;

                                //increases number of homes
                                main.sqlUtils.setData(Integer.toString(newNum), "UUID", uuid,
                                        databaseName + "num", "players");

                                //gets then sets the list of players homes
                                String hList = main.sqlUtils.getString(databaseName, "UUID", uuid,
                                        "players");
                                main.sqlUtils.setData(hList + homeID + " ", "UUID", uuid, databaseName,
                                        "players");

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
                                main.sqlUtils.createRow("homeID", uuid + homeID, databaseName);
                                main.sqlUtils.setData(uuid, "homeID", uuid + homeID,
                                        "playerId", databaseName);
                                main.sqlUtils.setData(world[1], "homeID", uuid + homeID,
                                        "world", databaseName);
                                main.sqlUtils.setData(Double.toString(x), "homeID", uuid + homeID,
                                        "x", databaseName);
                                main.sqlUtils.setData(Double.toString(y), "homeID", uuid + homeID,
                                        "y", databaseName);
                                main.sqlUtils.setData(Double.toString(z), "homeID", uuid + homeID,
                                        "z", databaseName);
                                main.sqlUtils.setData(Float.toString(yaw), "homeID", uuid + homeID,
                                        "yaw", databaseName);
                                main.sqlUtils.setData(Float.toString(pitch), "homeID", uuid + homeID,
                                        "pitch", databaseName);
                            } else {
                                player.sendMessage(red + "You already have a home set with this name please delete " +
                                        "it first!");
                                return true;
                            }
                        } else {
                            player.sendMessage(red + "Must have a home name after '/sethome'");
                            return true;
                        }
                    } else {
                        player.sendMessage(red + "You have too many homes! You cannot make more until you delete some!");
                        return true;
                    }
                } else if ("home".equalsIgnoreCase(label)) {
                    if (args.length > 0) {
                        String homeID = args[0];
                        String test = main.sqlUtils.getString("homeID", "homeID",
                                player.getUniqueId() + homeID, databaseName);

                        if ((player.getUniqueId() + homeID).equalsIgnoreCase(test)) {
                            String getWorld = main.sqlUtils.getString("world", "homeID",
                                    uuid + homeID, databaseName);
                            double x = main.sqlUtils.getDouble("x", "homeID",
                                    uuid + homeID, databaseName);
                            double y = main.sqlUtils.getDouble("y", "homeID",
                                    uuid + homeID, databaseName);
                            double z = main.sqlUtils.getDouble("z", "homeID",
                                    uuid + homeID, databaseName);
                            float yaw = main.sqlUtils.getFloat("yaw", "homeID",
                                    uuid + homeID, databaseName);
                            float pitch = main.sqlUtils.getFloat("pitch", "homeID",
                                    uuid + homeID, databaseName);

                            World world = Bukkit.getWorld(getWorld);
                            Location location = new Location(world, x, y, z, yaw, pitch);

                            Chunk chunk = player.getWorld().getChunkAt(location);
                            player.getWorld().loadChunk(chunk);


                            player.sendMessage(ChatColor.BLUE + "Teleporting to home " + ChatColor.GREEN + homeID +
                                    ChatColor.BLUE + " now!");
                            player.teleport(location);
                        } else {
                            player.sendMessage(red + "The home: " + ChatColor.DARK_RED + homeID + red +
                                    " doesn't exist");
                            return true;
                        }
                    } else {
                        player.sendMessage(red + "Must have a home name after '/home'");
                        return true;
                    }
                } else if ("delhome".equalsIgnoreCase(label)) {
                    if (args.length > 0) {
                        String homeID = args[0];
                        String test = main.sqlUtils.getString("homeID", "homeID", uuid + homeID,
                                databaseName);

                        if ((uuid + homeID).equalsIgnoreCase(test)) {
                            player.sendMessage(ChatColor.BLUE + "Home " + ChatColor.GREEN + homeID + ChatColor.BLUE +
                                    " was successfully deleted!");
                            int currentNum = getNumOfHomes--;
                            currentNum--;
                            main.sqlUtils.setData(Integer.toString(currentNum), "UUID", uuid, databaseName +
                                    "num", "players");
                            main.sqlUtils.remove("homeID", uuid + homeID, databaseName);
                            String hl = main.sqlUtils.getString(databaseName, "UUID", uuid,
                                    "players");
                            main.sqlUtils.setData(hl.replace(" " + homeID + " ", " "), "UUID",
                                    uuid, databaseName, "players");
                        } else {
                            player.sendMessage(red + "The home: " + ChatColor.DARK_RED + homeID + red +
                                    " needs to exist to delete it!");
                            return true;
                        }
                    } else {
                        player.sendMessage(red + "Must have a home name after '/delhome'");
                        return true;
                    }
                } else if ("homelist".equalsIgnoreCase(label)) {
                    String string = main.sqlUtils.getString(databaseName, "UUID", uuid, "players");
                    String[] list = string.split(" ");

                    StringBuilder homeList = new StringBuilder();

                    for (int i = 1; i < list.length; i++) {
                        homeList.append(ChatColor.GREEN).append(list[i]).append(ChatColor.WHITE).append(", ");
                    }

                    try {
                        player.sendMessage(ChatColor.BLUE + "You have " + ChatColor.GREEN + getNumOfHomes + "/" +
                                numOfHomes + ChatColor.BLUE + " homes, they are:");
                        player.sendMessage(homeList.substring(0, homeList.length() - 2));
                    } catch (StringIndexOutOfBoundsException e) {
                        player.sendMessage(red + "You have no homes set!");
                        return true;
                    }
                }
            }
            return true;

        } else {
            Player player = (Player) sender;
            player.sendMessage("Please enable homes in the config file");
        }
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1){
            if (s.equalsIgnoreCase("home") || s.equalsIgnoreCase("delhome")) {
                Player player = (Player) commandSender;
                String rawHomes = main.sqlUtils.getString(databaseName, "UUID",
                        player.getUniqueId().toString(), "players");
                String[] homes = rawHomes.split(" ");
                List<String> arg1 = new ArrayList<>();
                int len = homes.length - 1;
                while (len > 0) {
                    arg1.add(homes[len]);
                    len--;
                }
                final List<String> completions = new ArrayList<>();
                //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
                StringUtil.copyPartialMatches(strings[0], arg1, completions);
                //sort the list
                Collections.sort(completions);
                return completions;
            }
        }
        return null;
    }
}
