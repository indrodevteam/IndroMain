package indrocraft.indrocraftplugin.commands;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.dataManager.ConfigTools;
import indrocraft.indrocraftplugin.utils.RankUtils;
import indrocraft.indrocraftplugin.utils.SQLUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.advancement.Advancement;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class RankCommand implements TabExecutor {

    private final Main main = Main.getPlugin(Main.class);
    private final ConfigTools c = new ConfigTools(main, "rank.yml");
    private final FileConfiguration config = c.getConfig();
    private final RankUtils rankUtils = new RankUtils();
    private final SQLUtils sqlUtils = new SQLUtils(main);
    ChatColor red = ChatColor.RED;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        boolean state = config.getBoolean("useRanks");
        Player target;

        if (player.isOp()) {
            if (args.length > 0) {

                // sets editor args:
                boolean hasName = false;
                String name = null;
                String colour1 = null;
                String colour2 = null;
                boolean hasNextAdvancement = false;
                String nextAdvancement = null;
                boolean hasNextRank = false;
                String nextRank = null;
                boolean hasLevel = false;
                String level = null;

                for (int i = 1; i < args.length; i++) {

                    if (hasArgPrefix("name:", args[i])) {
                        name = args[i].toLowerCase().replaceFirst("name:", "");
                        hasName = true;
                    } else if (hasArgPrefix("colour1:", args[i])) {
                        colour1 = args[i].toLowerCase().replace("colour1:", "");
                    } else if (hasArgPrefix("colour2:", args[i])) {
                        colour2 = args[i].toLowerCase().replace("colour2:", "");
                    } else if (hasArgPrefix("nextadvance:", args[i])) {
                        nextAdvancement = args[i].toLowerCase().replace("nextadvance:", "");
                        hasNextAdvancement = true;
                    } else if (hasArgPrefix("nextrank:", args[i])) {
                        nextRank = args[i].toLowerCase().replace("nextrank:", "");
                        hasNextRank = true;
                    } else if (hasArgPrefix("level:", args[i])) {
                        level = args[i].toLowerCase().replace("level:", "");
                        hasLevel = true;
                    }
                }

                switch (args[0].toLowerCase()) {

                    //turn ranks on:

                    case "on":
                        if (state) {
                            player.sendMessage(red + "Ranks are already enabled nothing changed!");
                        } else {
                            player.sendMessage(ChatColor.GREEN + "enabling ranks");
                            config.set("useRanks", true);
                            c.saveConfig();
                        }
                        return true;

                    //turn ranks off:

                    case "off":
                        if (!state) {
                            player.sendMessage(red + "Ranks are already disabled nothing changed!");
                        } else {
                            player.sendMessage(ChatColor.GREEN + "disabling ranks");
                            config.set("useRanks", false);
                            c.saveConfig();
                        }
                        return true;

                    //set a players rank

                    case "set":
                        if (args.length >= 3) {
                            if (PlayerExist(args[1], player)) {
                                target = Bukkit.getPlayer(args[1]);
                                rankUtils.setRank(target, sqlUtils, args[2]);
                                rankUtils.LoadRank(target, sqlUtils);
                                player.sendMessage(ChatColor.BLUE + "Successfully set " + ChatColor.GREEN + args[1] +
                                        "'s " + ChatColor.BLUE + "rank to: " + ChatColor.GREEN + args[2]);
                                target.sendMessage(ChatColor.BLUE + "Successfully set your rank to: "
                                        + ChatColor.GREEN + args[2]);
                            }
                        } else {
                            player.sendMessage(red + "Arguments <Player> and <new rank> are required");
                        }
                        break;

                    //sets a players name colour:

                    case "setname":
                        if (args.length >= 3) {
                            if (PlayerExist(args[1], player)) {
                                target = Bukkit.getPlayer(args[1]);
                                rankUtils.setNameColour(target, sqlUtils, args[2]);
                                rankUtils.LoadRank(target, sqlUtils);
                                player.sendMessage(ChatColor.BLUE + "Successfully set " + ChatColor.GREEN + args[1] +
                                        "'s " + ChatColor.BLUE + "name colour to: " + ChatColor.GREEN + args[2]);
                                target.sendMessage(ChatColor.BLUE + "Successfully set your name colour to: "
                                        + ChatColor.GREEN + args[2]);
                            }
                        } else {
                            player.sendMessage(red + "Arguments <Player> and <name colour> are required");
                        }
                        break;

                    //===================================
                    //       Rank editor commands
                    //===================================

                    case "create":
                        // apply checks
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

                        // set data
                        //create the rank in the config file:
                        if (colour1 != null) {
                            config.set("ranks." + name + ".colours.primary", colour1);
                        } else {
                            config.set("ranks." + name + ".colours.primary", "WHITE");
                        }
                        if (colour2 != null) {
                            config.set("ranks." + name + ".colours.secondary", colour2);
                        } else {
                            config.set("ranks." + name + ".colours.secondary", "WHITE");
                        }
                        if (hasNextAdvancement)
                            config.set("ranks." + name + ".details.nextAdvancement", nextAdvancement);
                        if (hasNextRank)
                            config.set("ranks." + name + ".details.nextRank", nextRank);
                        if (hasLevel) {
                            config.set("ranks." + name + ".details.level", Integer.valueOf(level));
                        } else {
                            config.set("ranks." + name + ".details.level", 0);
                        }
                        c.saveConfig();
                        c.reloadConfig();

                        player.sendMessage(ChatColor.BLUE + "Successfully created rank: " + ChatColor.GREEN + name);

                        break;
                    case "delete":
                        if (args.length >= 2) {
                            if (rankExists(args[1])) {
                                config.set("ranks." + args[1], null);
                                c.saveConfig();
                                c.reloadConfig();
                                player.sendMessage(ChatColor.BLUE + "Successfully deleted rank: " + ChatColor.GREEN + args[1]);
                            } else {
                                player.sendMessage(red + "The specified rank does not exist!");
                            }
                        } else {
                            player.sendMessage(red + "Must have a valid rank name after 'delete'!");
                        }
                        break;
                    case "edit":
                        player.sendMessage(red + "unfinished please delete old rank and create new");
                        break;
                }
            } else {
                player.sendMessage(red + "Must have an argument after '/ranks'");
            }
        } else {
            player.sendMessage(red + "You do not have permission to do that");
        }
        c.reloadConfig();
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tabCompleter = new ArrayList<>();

        //sets the main argument
        if (args.length == 1)
            tabCompleter.addAll(Arrays.asList("on", "off", "set", "setname", "create", "delete", "edit"));

        //if the first arg isn't one of the rank editor argument a switch case handles tab completer
        if (!(args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("edit"))) {

            switch (args.length) {
                case 2:
                    if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("setname")) {
                        for (Player target : Bukkit.getOnlinePlayers())
                            tabCompleter.add(target.getName());
                    }

                    if (args[0].equalsIgnoreCase("delete"))
                        tabCompleter.addAll(config.getConfigurationSection("ranks").getKeys(false));

                    break;
                case 3:
                    if (args[0].equalsIgnoreCase("set")) {
                        c.reloadConfig();
                        tabCompleter.addAll(config.getConfigurationSection("ranks").getKeys(false));
                    }

                    if (args[0].equalsIgnoreCase("setname"))
                        tabCompleter.addAll(Arrays.asList("dark_red", "red", "gold", "yellow", "dark_green", "green",
                                "aqua", "dark_aqua", "dark_blue", "blue", "light_purple", "dark_purple", "white",
                                "gray", "dark_gray", "black"));
                    break;
            }
        } else {
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
                    tabCompleter.add("name:");
                }
                if (!hasC1) {
                    tabCompleter.add("colour1:");
                }
                if (!hasC2) {
                    tabCompleter.add("colour2:");
                }
                if (!hasAchievement) {
                    tabCompleter.add("nextAdvance:");
                }
                if (!hasRank) {
                    tabCompleter.add("nextRank:");
                }
                if (!hasLevel) {
                    tabCompleter.add("level:");
                }
            }
        }

        //create new array
        final List<String> completions = new ArrayList<>();
        //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
        StringUtil.copyPartialMatches(args[args.length - 1], tabCompleter, completions);
        String pre = args[args.length - 1];
        if (pre.toLowerCase().contains("colour1:")) {
            completions.removeAll(completions);
            completions.addAll(Arrays.asList("colour1:dark_red", "colour1:red", "colour1:gold", "colour1:yellow",
                    "colour1:dark_green", "colour1:green", "colour1:aqua", "colour1:dark_aqua", "colour1:dark_blue",
                    "colour1:blue", "colour1:light_purple", "colour1:dark_purple", "colour1:white", "colour1:gray",
                    "colour1:dark_gray", "colour1:black"));
        }
        if (pre.toLowerCase().contains("colour2:")) {
            completions.removeAll(completions);
            completions.addAll(Arrays.asList("colour2:dark_red", "colour2:red", "colour2:gold", "colour2:yellow",
                    "colour2:dark_green", "colour2:green", "colour2:aqua", "colour2:dark_aqua", "colour2:dark_blue",
                    "colour2:blue", "colour2:light_purple", "colour2:dark_purple", "colour2:white", "colour2:gray",
                    "colour2:dark_gray", "colour2:black"));
        }
        if (pre.toLowerCase().contains("nextrank:")) {
            completions.removeAll(completions);
            for (String ranks : config.getConfigurationSection("ranks").getKeys(false))
                completions.add("nextRank:" + ranks.toLowerCase(Locale.ROOT));
        }
        if (pre.toLowerCase().contains("nextadvance:")) {
            completions.removeAll(completions);
            for (Iterator<Advancement> it = Bukkit.advancementIterator(); it.hasNext(); ) {
                String advancement = "nextAdvance:" + it.next().getKey().getKey();
                if (!(advancement.toLowerCase().startsWith("nextadvance:r")))
                    completions.add(advancement);
            }
        }
        return completions;
    }

    // small methods
    public boolean PlayerExist(String playerName, Player sender) {
        try {
            Player target = Bukkit.getPlayer(playerName);
            return true;
        } catch (Exception e) {
            sender.sendMessage(red + "Can't find player specified make sure the name is spelt correctly!");
            return false;
        }
    }

    private boolean hasArgPrefix(String prefix, String arg) {
        return arg.toLowerCase().startsWith(prefix) && arg.length() > prefix.length();
    }

    private boolean rankExists (String rank) {
        List<String> arg1 = new ArrayList<>();
        try {
            for (String ranks : config.getConfigurationSection("ranks").getKeys(false))
                arg1.add(ranks.toLowerCase(Locale.ROOT));

            return arg1.contains(rank.toLowerCase());
        } catch (NullPointerException e) {
            return false;
        }
    }

    private boolean isNumber(String number) {
        try {
            int num = Integer.parseInt(number);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private String getTag(String arg) {
        int indexOfSplitter = arg.indexOf(':');
        if (indexOfSplitter > 0) {
            return arg.substring(0, indexOfSplitter);
        }
        return "";
    }
}
