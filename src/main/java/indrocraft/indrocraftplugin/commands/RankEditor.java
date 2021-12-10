package indrocraft.indrocraftplugin.commands;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.dataManager.ConfigTools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class RankEditor implements TabExecutor {

    // TODO: 3/12/2021
    //  - create rank
    //  - delete rank
    //  - edit rank
    //  - toggle ranks
    //  - list ranks

    ConfigTools c = new ConfigTools(Main.getPlugin(Main.class), "rank.yml");
    FileConfiguration config = c.getConfig();
    ChatColor red = ChatColor.RED;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender.isOp() && sender instanceof Player) {
            Player player = (Player) sender;

            boolean hasName = false;
            boolean hasC1 = false;
            boolean hasC2 = false;
            boolean hasAchive = false;
            boolean hasRank = false;
            boolean hasLevel = false;
            String name = null;
            String pColour = null;
            String sColour = null;
            String nextAdvancement = null;
            String nextRank = null;
            String level = null;

            for (int i = 1; i < args.length; i++) {
                //player.sendMessage(args[i]);
                if (hasArgPrefix("name:", args[i])) {
                    name = args[i].toLowerCase().replaceFirst("name:", "");
                    hasName = true;
                } else if (hasArgPrefix("Colour1:", args[i])) {
                    pColour = args[i].toLowerCase().replace("colour1:", "");
                    hasC1 = true;
                } else if (hasArgPrefix("Colour2:", args[i])) {
                    sColour = args[i].toLowerCase().replace("colour2:", "");
                    hasC2 = true;
                } else if (hasArgPrefix("nextAdvance:", args[i])) {
                    nextAdvancement = args[i].toLowerCase().replace("nextadvance:", "");
                    hasAchive = true;
                } else if (hasArgPrefix("nextRank:", args[i])) {
                    nextRank = args[i].toLowerCase().replace("nextrank:", "");
                    hasRank = true;
                } else if (hasArgPrefix("level:", args[i])) {
                    level = args[i].toLowerCase().replace("level:", "");
                    hasLevel = true;
                }
            }

            switch (args[0]){
                case "create":

                    if (!hasName) {
                        player.sendMessage(red + "Cannot create new rank! 'name:' parameter is required!");
                        return true;
                    } else if (rankExists(name)) {
                        player.sendMessage(red + "Rank already exist please choose another name!");
                        return true;
                    } else if (hasLevel && !isNumber(level)) {
                        player.sendMessage(red + "Invalid number for level!");
                        return true;
                    }


                    //create the rank in the config file:
                    if (hasC1) {
                        config.set("ranks." + name + ".colours.primary", pColour);
                    } else {
                        config.set("ranks." + name + ".colours.primary", "WHITE");
                    }
                    if (hasC2) {
                        config.set("ranks." + name + ".colours.secondary", sColour);
                    } else {
                        config.set("ranks." + name + ".colours.secondary", "WHITE");
                    }
                    if (hasAchive)
                        config.set("ranks." + name + ".details.nextAdvancement", nextAdvancement);
                    if (hasRank)
                        config.set("ranks." + name + ".details.nextRank", nextRank);
                    if (hasLevel) {
                        config.set("ranks." + name + ".details.level", Integer.valueOf(level));
                    } else {
                        config.set("ranks." + name + ".details.level", 0);
                    }
                    c.saveConfig();


                    break;
                case "delete":
                    if (rankExists(name)){
                        config.set("ranks." + name, null);
                    }

                    break;
                case "edit":
                        if (!rankExists(name)) {
                            player.sendMessage(red + "Rank must exist before it can be edited!");
                        } else {
                            if (hasAchive) {
                                config.set("ranks." + name + "details.nextAdvancement", nextAdvancement);
                            }

                            if (hasC1) {
                                config.set("ranks." + name + "colours.primary", pColour);
                            }

                            if (hasC2) {
                                config.set("ranks." + name + "colours.secondary", sColour);
                            }

                            if (hasLevel) {
                                config.set("ranks." + name + "details.level", level);
                            }

                            if (hasRank) {
                                config.set("ranks." + name + "details.nextRank", nextRank);
                            }
                        }
                    break;
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.addAll(Arrays.asList("create", "delete", "edit"));
            return list;
        }

        //tabCompleter for create subcommand
        if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("edit")) {
            boolean hasName = false;
            boolean hasC1 = false;
            boolean hasC2 = false;
            boolean hasAchievement = false;
            boolean hasRank = false;
            boolean hasLevel = false;

            //checks previous args:
            for (int i = 1; i < args.length; i++) {
                String argTag = getTag(args[i].toLowerCase());
                if (argTag.length() + 1 < args[i].length()) {
                    switch (argTag) {
                        case "name":
                            hasName = true;
                            break;
                        case "colour1":
                            hasC1 = true;
                            break;
                        case "colour2":
                            hasC2 = true;
                            break;
                        case "nextadvance":
                            hasAchievement = true;
                            break;
                        case "nextrank":
                            hasRank = true;
                            break;
                        case "level":
                            hasLevel = true;
                            break;
                    }
                }
            }

            //adds only unused parameters to tabCompleter
            if (!hasName) {
                list.add("name:");
            }
            if (!hasC1) {
                list.add("Colour1:");
            }
            if (!hasC2) {
                list.add("Colour2:");
            }
            if (!hasAchievement) {
                list.add("nextAdvance:");
            }
            if (!hasRank) {
                list.add("nextRank:");
            }
            if (!hasLevel) {
                list.add("level:");
            }
        }
        return list;
    }

    private boolean hasArgPrefix(String prefix, String arg) {
        return arg.toLowerCase().startsWith(prefix) && arg.length() > prefix.length();
    }

    private String getTag(String arg) {
        int indexOfSplitter = arg.indexOf(':');
        if (indexOfSplitter > 0) {
            return arg.substring(0, indexOfSplitter);
        }
        return "";
    }

    private boolean rankExists (String rank) {
        List<String> arg1 = new ArrayList<>();
        for (String ranks : config.getConfigurationSection("ranks").getKeys(false))
            arg1.add(ranks.toLowerCase(Locale.ROOT));


        return arg1.contains(rank.toLowerCase());
    }

    private boolean isNumber(String number) {
        try {
            int num = Integer.parseInt(number);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
