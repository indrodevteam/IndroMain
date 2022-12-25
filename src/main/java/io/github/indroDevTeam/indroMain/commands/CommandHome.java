package io.github.indroDevTeam.indroMain.commands;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.model.Point;
import io.github.indroDevTeam.indroMain.model.Profile;
import io.github.indroDevTeam.indroMain.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandHome implements TabExecutor {
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
        if (args.length != 1) {
            ChatUtils.syntaxError(sender);
            return false;
        }

        if (IndroMain.getDataManager().getPoint(player.getUniqueId(), args[0]).isEmpty()) {
            ChatUtils.sendFailure(player, "This point does not exist!");
            return true;
        }
        Point point = IndroMain.getDataManager().getPoint(player.getUniqueId(), args[0]).get();
        if (point.getDistance(player) >= profile.getRank().getMaxDistance()) {
            ChatUtils.sendFailure(player, "You're too far away to teleport there!");
            return true;
        }

        if (!profile.getRank().isCrossWorldPermitted() || !player.getLocation().getWorld().getName().equals(point.getLocation().getWorld().getName())) {
            ChatUtils.sendFailure(player, "This point is outside your dimension...");
            return true;
        }

        if (IndroMain.getCooldowns().checkTeleportStatus(player)) {
            ChatUtils.sendFailure(player, "You're already teleporting to somewhere else!");
            return true;
        }

        // teleport is cleared if it reaches here
        profile.warp(player, point);

        IndroMain.getDataManager().updateProfile(player.getUniqueId(), profile);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> arguments = new ArrayList<>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                List<Point> userList = IndroMain.getDataManager().getAllPoints();
                for (Point point: userList) {
                    arguments.add(point.getName());
                }
            }
        }
        return arguments;
    } 
}