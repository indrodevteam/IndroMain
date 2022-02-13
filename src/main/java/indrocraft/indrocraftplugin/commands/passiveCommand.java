package indrocraft.indrocraftplugin.commands;

import indrocraft.indrocraftplugin.Main;
import io.github.indroDevTeam.indroLib.MiscUtils;
import io.github.indroDevTeam.indroLib.datamanager.SQLUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class passiveCommand implements TabExecutor {

    SQLUtils sqlUtils = new SQLUtils(Main.getPlugin(Main.class).sqlconnector);
    ChatColor red = ChatColor.RED;
    ChatColor green = ChatColor.GREEN;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase("false")) {
                try {
                    Player player;
                    if (sender instanceof Player) {
                        if (sender.isOp())
                            player = Bukkit.getPlayer(args[1]);
                        else
                            player = (Player) sender;
                    } else if (args.length >= 2) {
                        player = Bukkit.getPlayer(args[1]);
                    } else {
                        return true;
                    }
                    MiscUtils mu = new MiscUtils();
                    mu.setPassive(player, args[0].equalsIgnoreCase("true"), sqlUtils);
                    if (player.isOnline()) {
                        if (args[0].equalsIgnoreCase("true"))
                            player.sendMessage(green + "You have been set to passive mode!");
                        else
                            player.sendMessage(green + "You are no longer in passive mode!");
                    }
                } catch (NullPointerException e) {
                    sender.sendMessage(red + "Invalid player name.");
                }
            } else {
                sender.sendMessage(red + "Invalid first argument. Must be wither true or false.");
            }
        } else {
            sender.sendMessage(red + "Not enough arguments.");
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        tab.add("true");
        tab.add("false");
        return tab;
    }
}
