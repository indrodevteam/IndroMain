package io.github.indroDevTeam.indroMain.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.fasterxml.jackson.databind.type.PlaceholderForType;

public class CommandHome implements TabExecutor {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> tabList = new ArrayList<>();
        switch (alias) {
            case "string" -> {

            }
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        

        switch (label) {
            case (home) {

            }
            case "sethome" -> {

            }
            case "delhome" -> {

            }
        }
        return false;
    }
    
}
