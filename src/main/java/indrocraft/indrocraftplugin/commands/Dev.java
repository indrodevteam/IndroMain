package indrocraft.indrocraftplugin.commands;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.dataManager.ConfigTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Dev implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ConfigTools config = new ConfigTools(Main.getPlugin(Main.class), "config.yml");

        List<String> list = config.getConfig().getStringList("locations");

        Player player = (Player) sender;
        player.sendMessage(String.valueOf(list.size()));
        return true;
    }
}
