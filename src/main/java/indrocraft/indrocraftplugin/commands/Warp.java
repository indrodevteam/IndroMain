package indrocraft.indrocraftplugin.commands;

import indrocraft.indrocraftplugin.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Warp implements CommandExecutor {

    public Main main;
    public Warp(Main main){this.main = main;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (label.equalsIgnoreCase("setwarp")) {

            } else if (label.equalsIgnoreCase("warp")) {

            }
        }
        return true;
    }
}
