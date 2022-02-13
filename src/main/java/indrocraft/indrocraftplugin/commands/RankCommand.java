package indrocraft.indrocraftplugin.commands;

import indrocraft.indrocraftplugin.Config;
import indrocraft.indrocraftplugin.Main;
import io.github.indroDevTeam.indroLib.datamanager.SQLUtils;
import io.github.indroDevTeam.indroLib.objects.ranks.Rank;
import io.github.indroDevTeam.indroLib.objects.ranks.RankEditor;
import io.github.indroDevTeam.indroLib.objects.ranks.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.advancement.Advancement;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RankCommand implements TabExecutor {

    private final Main main = Main.getPlugin(Main.class);
    private final Config CONFIG = new Config(main);
    private final SQLUtils sqlUtils = new SQLUtils(main.sqlconnector);

    //colours
    private final ChatColor red = ChatColor.RED;
    private final ChatColor green = ChatColor.GREEN;
    private final ChatColor blue = ChatColor.BLUE;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        boolean usingRanks = CONFIG.isRanks();

        if (usingRanks) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Player target;

                if (player.isOp()) {
                    if (args.length > 0) {
                        // sets editor args:
                        String id = null;
                        String displayName = null;
                        String leftBrace = null;
                        String rightBrace = null;
                        String primaryColour = null;
                        String secondaryColour = null;
                        String nextRankId = null;
                        String nextAdvancement = null;
                        String level = null;

                        for (int i = 1; i < args.length; i++) {
                            //get arguments as strings

                            if (hasArgPrefix("id:", args[i])) {
                                id = getArg(args[i]);
                            } else if (hasArgPrefix("displayname:", args[i])) {
                                displayName = getArg(args[i]);
                            } else if (hasArgPrefix("leftbrace:", args[i])) {
                                leftBrace = getArg(args[i]);
                            } else if (hasArgPrefix("rightbrace:", args[i])) {
                                rightBrace = getArg(args[i]);
                            } else if (hasArgPrefix("primarycolour:", args[i])) {
                                primaryColour = getArg(args[i]);
                            } else if (hasArgPrefix("secondarycolour:", args[i])) {
                                secondaryColour = getArg(args[i]);
                            } else if (hasArgPrefix("nextrankid:", args[i])) {
                                nextRankId = getArg(args[i]);
                            } else if (hasArgPrefix("nextadvancement:", args[i])) {
                                nextAdvancement = getArg(args[i]);
                            } else if (hasArgPrefix("level:", args[i])) {
                                level = getArg(args[i]);
                            }
                        }

                        switch (args[0].toLowerCase()) {

                            //set a players rank

                            case "set":
                                if (args.length >= 3) {
                                    if (playerExist(args[1], player)) {
                                        target = Bukkit.getPlayer(args[1]);
                                        assert target != null;
                                        RankUtils.setPlayerRank(target, args[2], sqlUtils);
                                        RankUtils.loadPlayerRank(target, sqlUtils);
                                        player.sendMessage(blue + "Successfully set " + green + args[1]
                                                + "'s " + blue + "rank to: " + green + args[2]);
                                        target.sendMessage(blue + "Successfully set your rank to: "
                                                + green + args[2]);
                                    }
                                } else {
                                    player.sendMessage(red + "Arguments <Player> and <new rank> are required");
                                }
                                break;

                            //sets a players name colour:

                            case "setname":
                                if (args.length >= 3) {
                                    if (playerExist(args[1], player)) {
                                        target = Bukkit.getPlayer(args[1]);
                                        assert target != null;
                                        RankUtils.setPlayerNameColour(target, args[2], sqlUtils);
                                        RankUtils.loadPlayerRank(target, sqlUtils);
                                        player.sendMessage(blue + "Successfully set " + green + args[1]
                                                + "'s " + blue + "name colour to: " + green + args[2]);
                                        target.sendMessage(blue + "Successfully set your name colour to: "
                                                + green + args[2]);
                                    }
                                } else {
                                    player.sendMessage(red + "Arguments <Player> and <name colour> are required");
                                }
                                break;

                            //===================================
                            //       Rank editor commands
                            //===================================

                            case "create":
                                if (RankUtils.rankExist(id, sqlUtils)) {
                                    player.sendMessage(red + "Rank already exist please choose another id!");
                                    return true;
                                }

                            case "edit":
                                // apply checks
                                if (displayName == null || id == null) {
                                    player.sendMessage(red +
                                            "Cannot create new rank! 'displayname:' and 'id:' parameters are required!"
                                    );
                                    return true;
                                }

                                // set data
                                //create the rank in the rankPresets table:
                                RankEditor re = new RankEditor(id, displayName);
                                if (primaryColour != null) re.setPrimary(RankUtils.readColour(primaryColour));
                                if (secondaryColour != null) re.setSecondary(RankUtils.readColour(secondaryColour));
                                if (nextRankId != null) re.setNextRankId(nextRankId);
                                if (nextAdvancement != null)
                                    re.setNextAdvancement(RankUtils.getAdvancement(nextAdvancement));
                                if (!isNumber(level)) level = "0";
                                if (level != null) re.setLevel(Integer.parseInt(level));
                                if (leftBrace == null && rightBrace == null) re.setBraces("[", "]");
                                if (leftBrace == null && rightBrace != null) re.setBraces("[", rightBrace);
                                if (leftBrace != null && rightBrace == null) re.setBraces(leftBrace, "]");
                                if (leftBrace != null && rightBrace != null) re.setBraces(leftBrace, rightBrace);

                                re.save(sqlUtils);

                                player.sendMessage(blue + "Successfully created rank: " + green + displayName);

                                break;
                            case "delete":
                                if (args.length >= 2) {
                                    if (RankUtils.rankExist(args[1], sqlUtils)) {
                                        RankUtils.deleteRank(args[1], sqlUtils);
                                        player.sendMessage(blue + "Successfully deleted rank: " + green + args[1]);
                                    } else player.sendMessage(red + "The specified rank does not exist!");
                                } else player.sendMessage(red + "Must have a valid rank name after 'delete'!");
                                break;
                        }
                    } else {
                        player.sendMessage(red + "Must have an argument after '/ranks'");
                    }
                } else {
                    player.sendMessage(red + "You do not have permission to do that");
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> tabCompleter = new ArrayList<>();
        List<Rank> ranks = RankUtils.getRanks(sqlUtils);

        //sets the main argument
        if (args.length == 1)
            tabCompleter.addAll(Arrays.asList("set", "setname", "create", "delete", "edit"));

        //if the first arg isn't one of the rank editor argument a switch case handles tab completer
        if (!(args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("edit"))) {

            switch (args.length) {
                case 2:
                    if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("setname")) {
                        for (Player target : Bukkit.getOnlinePlayers())
                            tabCompleter.add(target.getName());
                    }

                    if (args[0].equalsIgnoreCase("delete"))
                        for (Rank rank : ranks)
                            tabCompleter.add(rank.getId());

                    break;
                case 3:
                    if (args[0].equalsIgnoreCase("set"))
                        for (Rank rank : ranks)
                            tabCompleter.add(rank.getId());

                    if (args[0].equalsIgnoreCase("setname"))
                        tabCompleter.addAll(Arrays.asList("dark_red", "red", "gold", "yellow", "dark_green", "green",
                                "aqua", "dark_aqua", "dark_blue", "blue", "light_purple", "dark_purple", "white",
                                "gray", "dark_gray", "black"));
                    break;
            }
        } else {
            if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("edit")) {
                boolean hasId = false;
                boolean lbrace = false;
                boolean rbrace = false;
                boolean hasName = false;
                boolean hasC1 = false;
                boolean hasC2 = false;
                boolean hasAchievement = false;
                boolean hasRank = false;
                boolean hasLevel = false;

                //checks previous args:
                for (int i = 1; i < args.length; i++) {
                    String argTag = getPrefix(args[i].toLowerCase());
                    if (argTag.length() + 1 < args[i].length()) {
                        switch (argTag) {
                            case "id":
                                hasId = true;
                                break;
                            case "displayname":
                                hasName = true;
                                break;
                            case "leftbrace":
                                lbrace = true;
                                break;
                            case "rightbrace":
                                rbrace = true;
                                break;
                            case "primarycolour":
                                hasC1 = true;
                                break;
                            case "secondarycolour":
                                hasC2 = true;
                                break;
                            case "nextadvancement":
                                hasAchievement = true;
                                break;
                            case "nextrankid":
                                hasRank = true;
                                break;
                            case "level":
                                hasLevel = true;
                                break;
                        }
                    }
                }

                //adds only unused parameters to tabCompleter
                if (!hasId) tabCompleter.add("id:");
                if (!hasName) tabCompleter.add("displayname:");
                if (!lbrace) tabCompleter.add("leftbrace:");
                if (!rbrace) tabCompleter.add("rightbrace:");
                if (!hasC1) tabCompleter.add("primarycolour:");
                if (!hasC2) tabCompleter.add("secondarycolour:");
                if (!hasAchievement) tabCompleter.add("nextadvancement:");
                if (!hasRank) tabCompleter.add("nextrankid:");
                if (!hasLevel) tabCompleter.add("level:");
            }
        }

        //create new array
        final List<String> completions = new ArrayList<>();
        //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
        StringUtil.copyPartialMatches(args[args.length - 1], tabCompleter, completions);
        String pre = args[args.length - 1];
        if (pre.toLowerCase().contains("primarycolour:")) {
            completions.clear();
            completions.addAll(Arrays.asList("primarycolour:dark_red", "primarycolour:red", "primarycolour:gold",
                    "primarycolour:yellow", "primarycolour:dark_green", "primarycolour:green", "primarycolour:aqua",
                    "primarycolour:dark_aqua", "primarycolour:dark_blue", "primarycolour:blue",
                    "primarycolour:light_purple", "primarycolour:dark_purple", "primarycolour:white",
                    "primarycolour:gray", "primarycolour:dark_gray", "primarycolour:black"));
        }
        if (pre.toLowerCase().contains("secondarycolour:")) {
            completions.clear();
            completions.addAll(Arrays.asList("secondarycolour:dark_red", "secondarycolour:red", "secondarycolour:gold",
                    "secondarycolour:yellow", "secondarycolour:dark_green", "secondarycolour:green",
                    "secondarycolour:aqua", "secondarycolour:dark_aqua", "secondarycolour:dark_blue",
                    "secondarycolour:blue", "secondarycolour:light_purple", "secondarycolour:dark_purple",
                    "secondarycolour:white", "secondarycolour:gray",
                    "secondarycolour:dark_gray", "secondarycolour:black"));
        }
        if (pre.toLowerCase().contains("nextrank:")) {
            completions.clear();
            for (Rank rank : ranks)
                completions.add("nextRank:" + rank.getId());
        }
        if (pre.toLowerCase().contains("nextadvance:")) {
            completions.clear();
            for (Iterator<Advancement> it = Bukkit.advancementIterator(); it.hasNext(); ) {
                String advancement = "nextAdvance:" + it.next().getKey().getKey();
                if (!(advancement.toLowerCase().startsWith("nextadvance:r")))
                    completions.add(advancement);
            }
        }
        return completions;
    }

    // small methods
    public boolean playerExist(String playerName, Player sender) {
        try {
            Bukkit.getPlayer(playerName);
            return true;
        } catch (Exception e) {
            sender.sendMessage(red + "Can't find player specified make sure the name is spelt correctly!");
            return false;
        }
    }

    private boolean hasArgPrefix(String prefix, String arg) {
        return arg.toLowerCase().startsWith(prefix) && arg.length() > prefix.length();
    }

    private boolean isNumber(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getArg(String arg) {
        int indexOfSplitter = arg.indexOf(':');
        if (indexOfSplitter > 0) {
            return arg.substring(indexOfSplitter + 1);
        }
        return "";
    }

    private String getPrefix(String arg) {
        int indexOfSplitter = arg.indexOf(':');
        if (indexOfSplitter > 0) {
            return arg.substring(0, indexOfSplitter);
        }
        return "";
    }
}
