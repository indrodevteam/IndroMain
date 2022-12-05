package io.github.indroDevTeam.indroMain.commands;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.model.Home;
import io.github.indroDevTeam.indroMain.model.Profile;
import io.github.indroDevTeam.indroMain.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.PlainDocument;

public class CommandHome implements CommandExecutor {
    private IndroMain pl;

    public CommandHome(IndroMain pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            ChatUtils.notPlayerError(commandSender);
            return true;
        }

        Home home = null;

        Profile profile = pl.getProfiles().getConfig().getSerializable(player.getUniqueId().toString(), Profile.class);

        if (args.length == 1) {
            if (home.getDistance(player) >= profile.getRank().getMaxDistance()) {
                ChatUtils.sendError(player, "You're too far away to teleport there!");
                return true;
            }

            if (!profile.getRank().isCrossWorldPermitted() && !player.getLocation().getWorld().getName().equals(home.getLocation().getWorld().getName())) {
                ChatUtils.sendError(player, "This point is outside your dimension...");
                return true;
            }

            // teleport is cleared if the teleport is there...
            profile.warp(player, home);
            return true;
        }
        ChatUtils.tooManyArgs(commandSender);
        return true;
    }
}
