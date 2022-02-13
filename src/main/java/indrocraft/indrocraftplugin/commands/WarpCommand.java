package indrocraft.indrocraftplugin.commands;

import indrocraft.indrocraftplugin.Config;
import indrocraft.indrocraftplugin.Main;
import io.github.indroDevTeam.indroLib.datamanager.SQLUtils;
import io.github.indroDevTeam.indroLib.objects.warps.Warp;
import io.github.indroDevTeam.indroLib.objects.warps.WarpUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WarpCommand implements TabExecutor {

    private final Main MAIN = Main.getPlugin(Main.class);
    private final Config C = new Config(MAIN);
    private final SQLUtils SQL = new SQLUtils(MAIN.sqlconnector);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ChatColor red = ChatColor.RED;
        ChatColor green = ChatColor.GREEN;
        ChatColor blue = ChatColor.BLUE;

        if (C.isWarps()) {
            if (args.length >= 1) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.isOp()) {
                        switch (label.toLowerCase()) {
                            case "setwarp":
                                if (WarpUtils.warpExist(args[0], SQL)) {
                                    WarpUtils.createWarp(args[0], player.getLocation(), SQL);
                                    player.sendMessage(green + "Successfully created warp: " + blue + args[0]);
                                } else player.sendMessage(red + "Dam someone beat you to it! A warp with this name already exists");
                                break;
                            case "delwarp":
                                if (!WarpUtils.warpExist(args[0], SQL)) {
                                    WarpUtils.deleteWarp(args[0], SQL);
                                    player.sendMessage(green + "Successfully deleted warp: " + blue + args[0]);
                                } else player.sendMessage(red +
                                        "Sorry can't find this one on the map, it needs to exist for me to delete it"
                                );
                                break;
                            case "warp":
                                if (!WarpUtils.warpExist(args[0], SQL)) {
                                    WarpUtils.teleportWarp(player, WarpUtils.getWarp(args[0], SQL), false);
                                    player.sendMessage(green + "Successfully teleported to: " + blue + args[0]);
                                } else player.sendMessage(red +
                                        "Are you sure you know your address? This home doesn't exist."
                                );
                                break;
                        }
                    } else if (label.equalsIgnoreCase("warp")) {
                        if (!WarpUtils.warpExist(args[0], SQL)) {
                            WarpUtils.teleportWarp(player, WarpUtils.getWarp(args[0], SQL), false);
                            player.sendMessage(green + "Successfully teleported to: " + blue + args[0]);
                        } else player.sendMessage(red +
                                "Are you sure you know your address? This home doesn't exist."
                        );
                    }
                } else sender.sendMessage(red + "EASTER EGG 🥚. (ran out of ideas and console cant warp)");
            } else sender.sendMessage(red + "You forgot to put a name! better luck next time.");
        } else sender.sendMessage(
                red + "Please enable warps in the Config file (cuase I worked really hard on them, tell an admin)"
        );
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] strings) {
        if (alias.equalsIgnoreCase("warp") || alias.equalsIgnoreCase("delwarp")) {
            List<Warp> setWarps = WarpUtils.getWarps(SQL);
            List<String> tab = new ArrayList<>();

            for (Warp warp : setWarps) tab.add(warp.getWarpName());

            //create new array
            final List<String> completions = new ArrayList<>();
            //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
            StringUtil.copyPartialMatches(strings[0], tab, completions);
            //sort the list
            Collections.sort(completions);
            return completions;
        } else {
            return null;
        }
    }
}
