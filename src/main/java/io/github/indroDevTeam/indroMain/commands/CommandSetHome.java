package io.github.indroDevTeam.indroMain.commands;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.model.Point;
import io.github.indroDevTeam.indroMain.model.Profile;
import io.github.indroDevTeam.indroMain.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandSetHome implements CommandExecutor {
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

        if (IndroMain.getDataManager().getPoint(player.getUniqueId(), args[0]).isPresent()) {
            ChatUtils.sendFailure(player, "A point with this name already exists!");
            return true;
        }

        if (IndroMain.getDataManager().getPointByOwner(player.getUniqueId()).size() >= profile.getRank().getWarpCap()) {
            ChatUtils.sendFailure(player, "You've made too many points to create another one!");
            return true;
        }

        Point point = new Point(player.getUniqueId(), args[0], player.getLocation());


        return true;
    }
}
