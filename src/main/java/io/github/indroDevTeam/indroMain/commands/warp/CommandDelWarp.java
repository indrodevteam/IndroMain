package io.github.indroDevTeam.indroMain.commands.warp;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.dataUtils.LanguageLoader;
import io.github.indroDevTeam.indroMain.teleports.Point;
import io.github.indroDevTeam.indroMain.teleports.PointStorage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandDelWarp implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("You cannot delete a warp in the console!");
        } else {
            if (label.equalsIgnoreCase("delwarp")) {
                if (args.length == 1) {
                    if (player.isOp()) {
                        Point point = PointStorage.findPoint(IndroMain.getInstance().getServer().getName(), args[0]);
                        if (point != null) {
                            PointStorage.deletePoint(point.getHomeName());
                            player.sendMessage(new LanguageLoader().get("plugin-title") + new LanguageLoader().get("del-warp-success"));
                        } else {
                            player.sendMessage(new LanguageLoader().get("plugin-title") + new LanguageLoader().get("error-point-exist"));
                        }
                    }
                }
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> arguments = new ArrayList<>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                ArrayList<Point> userList = PointStorage.findPointsWithOwner(IndroMain.getInstance().getServer().getName());
                for (Point point:
                        userList) {
                    arguments.add(point.getHomeName());
                }
            }
        }
        return arguments;
    }
}
