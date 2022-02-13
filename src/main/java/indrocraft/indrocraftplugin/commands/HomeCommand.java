package indrocraft.indrocraftplugin.commands;

import indrocraft.indrocraftplugin.Config;
import indrocraft.indrocraftplugin.Main;
import io.github.indroDevTeam.indroLib.datamanager.SQLUtils;
import io.github.indroDevTeam.indroLib.objects.homes.Home;
import io.github.indroDevTeam.indroLib.objects.homes.HomeUtils;
import io.github.indroDevTeam.indroLib.objects.ranks.RankUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeCommand implements TabExecutor {

    private final Main MAIN = Main.getPlugin(Main.class);
    private final SQLUtils SQL = new SQLUtils(MAIN.sqlconnector);
    private final Config CONFIG = new Config(MAIN);


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        ChatColor red = ChatColor.RED;
        ChatColor green = ChatColor.GREEN;
        ChatColor blue = ChatColor.BLUE;

        if (CONFIG.isHomes()) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length >= 1 && !label.equalsIgnoreCase("homelist")) {
                    switch (label.toLowerCase()) {
                        case "sethome":
                            if (!HomeUtils.homeExist(player, args[0], SQL)) {
                                if (CONFIG.isRanks()) {
                                    int homesCount = HomeUtils.getPlayerHomeAmount(player, SQL);
                                    int homesTotal = RankUtils.getRank(player, SQL).getLevel();
                                    //need to add get name colour as well
                                    if (homesCount == homesTotal) {
                                        player.sendMessage(red + "You're quite the the traveler! Delete a home before set a new one.");
                                        return true;
                                    }
                                }
                                HomeUtils.createHome(args[0], player.getLocation(), player, SQL);
                                player.sendMessage(green + "Successfully created home: " + blue + args[0]);
                            } else
                                player.sendMessage(red + "Sorry you've already got this one! Try naming it something else.");
                            break;
                        case "delhome":
                            if (HomeUtils.homeExist(player, args[0], SQL)) {
                                HomeUtils.deleteHome(HomeUtils.getHome(args[0], player, SQL), SQL);
                                player.sendMessage(green + "Successfully deleted home: " + blue + args[0]);
                            } else
                                player.sendMessage(red + "Are you sure you own this home? A home needs to exist before you can delete it.");
                            break;
                        case "home":
                            if (HomeUtils.homeExist(player, args[0], SQL)) {
                                HomeUtils.teleportHome(HomeUtils.getHome(args[0], player, SQL));
                                player.sendMessage(green + "Successfully teleported to: " + blue + args[0]);
                            } else
                                player.sendMessage(red + "Are you sure you know your address? This home doesn't exist.");
                            break;
                    }
                } else {
                    player.sendMessage(red + "You forgot to put a name! better luck next time.");
                }
                if (label.equalsIgnoreCase("homelist")) {
                    if (CONFIG.isRanks()) {
                        int homesCount = HomeUtils.getPlayerHomeAmount(player, SQL);
                        int homesTotal = RankUtils.getRank(player, SQL).getLevel();
                        player.sendMessage(blue + "You have " + green + homesCount + "/" +
                                homesTotal + blue + " homes, they are:");
                    } else player.sendMessage(blue + "Your set homes are:");
                    if (HomeUtils.getPlayerHomeAmount(player, SQL) == 0) {
                        StringBuilder sb = new StringBuilder();
                        List<Home> homeList = HomeUtils.getPlayersHomes(player, SQL);
                        for (Home home : homeList)
                            if (home != homeList.get(homeList.size() - 1))
                                sb.append(blue).append(home.getName()).append(green).append(", ");
                            else
                                sb.append(blue).append(home.getName());
                        player.sendMessage(sb.toString());
                    } else {
                        player.sendMessage(red + "You have no homes set!");
                        return true;
                    }
                }
            } else {
                sender.sendMessage(red + "You try as hard as you can but you can't teleport! " +
                        "you do fart a rainbow tho...");
            }
        } else {
            sender.sendMessage(red + "Please enable homes in the Config file (cuase I worked really hard on them tell an admin)");
        }
        return true;
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        if (strings.length == 1){
            if (s.equalsIgnoreCase("home") || s.equalsIgnoreCase("delhome")) {
                if (commandSender instanceof Player) {
                    List<Home> homeList = HomeUtils.getPlayersHomes((Player) commandSender, SQL);
                    List<String> tab = new ArrayList<>();
                    for (Home home : homeList)
                        tab.add(home.getName());
                    final List<String> completions = new ArrayList<>();
                    //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
                    StringUtil.copyPartialMatches(strings[0], tab, completions);
                    //sort the list
                    Collections.sort(completions);
                    return completions;
                }
            }
        }
        return null;
    }
}
