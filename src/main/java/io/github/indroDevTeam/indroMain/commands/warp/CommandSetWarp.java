package io.github.indroDevTeam.indroMain.commands.warp;

import io.github.indroDevTeam.indroMain.dataUtils.LanguageLoader;
import io.github.indroDevTeam.indroMain.teleports.PointUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommandSetWarp implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("You cannot set a warp in the console!");
        } else {
            if (label.equalsIgnoreCase("setwarp")) {
                if (args.length == 1) {
                    if (player.isOp()) {
                        PointUtils.createWarp(args[0], player.getLocation());
                        player.sendMessage(new LanguageLoader().get("plugin-title") + new LanguageLoader().get("set-warp-success"));
                    } else {
                        player.sendMessage(new LanguageLoader().get("plugin-title") + new LanguageLoader().get("error-permission"));
                    }
                } else {
                    player.sendMessage(new LanguageLoader().get("plugin-title") + new LanguageLoader().get("error-syntax"));
                }
            }
        }
        return true;
    }
}
