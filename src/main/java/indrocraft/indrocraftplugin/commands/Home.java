package indrocraft.indrocraftplugin.commands;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.dataManager.ConfigTools;
import indrocraft.indrocraftplugin.utils.RankUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Home implements TabExecutor {
    private final Main main;

    public Home(Main main) {this.main = main;}

    FileConfiguration config = ConfigTools.getFileConfig("rank.yml");
    FileConfiguration configA = ConfigTools.getFileConfig("config.yml");
    public String databaseName = config.getString("databaseForTP");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ChatColor red = ChatColor.RED;
        Boolean homeEnabled = configA.getBoolean("homes");

        if (homeEnabled) {

            if (sender instanceof Player) {
                String homeID = args[0].toLowerCase();
                Player player = (Player) sender;
                String uuid = player.getUniqueId().toString();
                int getNumOfHomes = main.sqlUtils.getInt(databaseName + "num", "UUID", player.getUniqueId().toString(), "players");
                int numOfHomes = 0;
                int playerLevel = RankUtils.getLevel(player, main.sqlUtils);
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
                //gets number of homes using: pRank.getLevel()
                if ("sethome".equalsIgnoreCase(label)) {
                    if (getNumOfHomes < numOfHomes) {
                        if (args.length > 0) {
                            String homesList = main.sqlUtils.getString(databaseName, uuid, databaseName, "players");
                            if (!(homesList.contains(" " + homeID + " "))) {
                                player.sendMessage(ChatColor.BLUE + "Successfully set home: " + ChatColor.GREEN + homeID);
                                Integer newNum = getNumOfHomes + 1;

                                //increases number of homes
                                main.sqlUtils.setData(newNum.toString(), "UUID", uuid, databaseName + "num", "players");

                                //gets then sets the list of players homes
                                String hList = main.sqlUtils.getString(databaseName, "UUID", uuid, "players");
                                main.sqlUtils.setData(hList + homeID + " ", "UUID", uuid, databaseName, "players");

                                String getWorld = player.getWorld().toString();
                                getWorld = getWorld.substring(0, getWorld.length() - 1);
                                String[] world = getWorld.split("\\=");

                                //location and direction of player:
                                Double x = player.getLocation().getX();
                                Double y = player.getLocation().getY();
                                Double z = player.getLocation().getZ();
                                Float yaw = player.getLocation().getYaw();
                                Float pitch = player.getLocation().getPitch();

                                //creates new row and fills in location data
                                main.sqlUtils.createRow("homeID", uuid + homeID, databaseName);
                                main.sqlUtils.setData(world[1], "homeID", uuid + homeID,"world", databaseName);
                                main.sqlUtils.setData(x.toString(), "homeID", uuid + homeID,"x", databaseName);
                                main.sqlUtils.setData(y.toString(), "homeID", uuid + homeID,"y", databaseName);
                                main.sqlUtils.setData(z.toString(), "homeID", uuid + homeID,"z", databaseName);
                                main.sqlUtils.setData(yaw.toString(), "homeID", uuid + homeID,"yaw", databaseName);
                                main.sqlUtils.setData(pitch.toString(), "homeID", uuid + homeID,"pitch", databaseName);
                            } else {
                                player.sendMessage(red + "You already have a home set with this name please delete it first!");
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
                        String test = main.sqlUtils.getString("homeID", "UUID", player.getUniqueId() + homeID, databaseName);

                        if ((player.getUniqueId() + homeID).equalsIgnoreCase(test)) {
                            String getWorld = main.sqlUtils.getString("world", "homeID", uuid + homeID, databaseName);
                            double x = main.sqlUtils.getDouble("x", "homeID", uuid + homeID, databaseName);
                            double y = main.sqlUtils.getDouble("y", "homeID", uuid + homeID, databaseName);
                            double z = main.sqlUtils.getDouble("z", "homeID", uuid + homeID, databaseName);
                            float yaw = main.sqlUtils.getFloat("yaw", "homeID", uuid + homeID, databaseName);
                            float pitch = main.sqlUtils.getFloat("pitch", "homeID", uuid + homeID, databaseName);

                            World world = Bukkit.getWorld(getWorld);
                            Location location = new Location(world, x, y, z, yaw, pitch);

                            Chunk chunk = player.getWorld().getChunkAt(location);
                            player.getWorld().loadChunk(chunk);


                            player.sendMessage(ChatColor.BLUE + "Teleporting to home " + ChatColor.GREEN + homeID + ChatColor.BLUE + " now!");
                            player.teleport(location);
                        } else {
                            player.sendMessage(red + "The home: " + ChatColor.DARK_RED + homeID + red + " doesn't exist");
                            return true;
                        }
                    } else {
                        player.sendMessage(red + "Must have a home name after '/home'");
                        return true;
                    }
                } else if ("delhome".equalsIgnoreCase(label)) {

                    if (args.length > 0) {
                        String test = main.sqlUtils.getString("NAME", "homeID", uuid + homeID, databaseName);

                        if ((player.getUniqueId() + homeID).equalsIgnoreCase(test)) {
                            player.sendMessage(ChatColor.BLUE + "Home " + ChatColor.GREEN + homeID + ChatColor.BLUE + " was successfully deleted!");
                            Integer currentNum = main.sqlUtils.getInt(databaseName + "num", "homeID", uuid + homeID, "players") - 1;
                            main.sqlUtils.setData(currentNum.toString(), "homeID", uuid + homeID, databaseName + "num", "players");
                            main.sqlUtils.remove("homeID", uuid + homeID, databaseName);
                            String hl = main.sqlUtils.getString(databaseName, "UUID", uuid, "players");
                            main.sqlUtils.setData(hl.replace(" " + homeID + " ", " "), "UUID", uuid, databaseName, "players");
                        } else {
                            player.sendMessage(red + "The home: " + ChatColor.DARK_RED + homeID + red + " needs to exist to delete it!");
                            return true;
                        }
                    } else {
                        player.sendMessage(red + "Must have a home name after '/delhome'");
                        return true;
                    }
                } else if ("homelist".equalsIgnoreCase(label)) {
                    String string = main.sqlUtils.getString(databaseName, "UUID", uuid, "players");
                    String[] list = string.split(" ");

                    String homeList = "";

                    for (int i = 1; i < list.length; i++) {
                        homeList = homeList + ChatColor.GREEN + list[i] + ChatColor.WHITE + ", ";
                    }

                    try {
                        player.sendMessage(ChatColor.BLUE + "You have " + ChatColor.GREEN + getNumOfHomes + "/" + numOfHomes + ChatColor.BLUE + " homes, they are:");
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
                String rawHomes = main.sqlUtils.getString(databaseName, "UUID", player.getUniqueId().toString(), "players");
                String[] homes = rawHomes.split(" ");
                List<String> arg1 = new ArrayList<>();
                int len = homes.length - 1;
                while (len > 0) {
                    arg1.add(homes[len]);
                    len--;
                }
                return arg1;
            }
        }
        return null;
    }
}
