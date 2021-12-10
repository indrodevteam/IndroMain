package indrocraft.indrocraftplugin.commands;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.dataManager.ConfigTools;
import indrocraft.indrocraftplugin.utils.RankUtils;
import indrocraft.indrocraftplugin.utils.SQLUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Dev implements CommandExecutor{

    private SQLUtils data = Main.getPlugin(Main.class).sqlUtils;
    private RankUtils rankUtils = new RankUtils();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ConfigTools config = new ConfigTools(Main.getPlugin(Main.class), "rank.yml");

        Player player = (Player) sender;

        rankUtils.setRank(player, data, "default");
        rankUtils.LoadRank(player, data);
        return true;
    }
}
