package io.github.indroDevTeam.indroMain.commands;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.model.Point;
import io.github.indroDevTeam.indroMain.model.Profile;
import io.github.indroDevTeam.indroMain.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CommandHomes implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            ChatUtils.notPlayerError(sender);
            return true;
        }

        Profile profile;
        if (IndroMain.getDataManager().getProfile(player.getUniqueId()).isEmpty()) {
            IndroMain.getDataManager().createProfile(Profile.getNewProfile(player, "default"));
        }
        profile = IndroMain.getDataManager().getProfile(player.getUniqueId()).get();

        /* Run checks for validity */
        if (args.length > 0) {
            ChatUtils.syntaxError(sender);
            return false;
        }

        List<Point> points = IndroMain.getDataManager().getPointByOwner(player.getUniqueId());
        points.sort((o1, o2) -> (int) (o1.getDistance(player) + o2.getDistance(player)));

        List<String> messages = new ArrayList<>();
        messages.add(ChatColor.BLUE + "-----+ Homes +-----");
        for (Point p: points) messages.add(String.format(ChatColor.BLUE + "%d m - %s", Math.round(p.getDistance(player)), p.getName()));
        messages.add(String.format(ChatColor.BLUE + "-----+ %d/%d homes +-----", points.size(), profile.getRank().getWarpCap()));

        for (String s: messages) player.sendMessage(s);
        return true;
    }
}
