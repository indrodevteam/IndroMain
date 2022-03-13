package io.github.indroDevTeam.indroMain.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

public class CommandTime implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("time") && args.length == 1) {
            int seconds = Math.toIntExact(sender.getServer().getWorld(args[0]).getTime() / 72);
            LocalTime time = LocalTime.ofSecondOfDay(seconds);

            sender.sendMessage(
    ChatColor.AQUA + "-------------------------",
                ChatColor.AQUA + "| The time is: " + ChatColor.RED + time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                ChatColor.AQUA + "-------------------------"
            );
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 1) {
            for (World world : Bukkit.getServer().getWorlds()) {
                arguments.add(world.getName());
            }
        }
        return arguments;
    }
}
