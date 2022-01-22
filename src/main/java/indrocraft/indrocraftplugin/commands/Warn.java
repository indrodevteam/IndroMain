package indrocraft.indrocraftplugin.commands;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.utils.ConfigUtils;
import indrocraft.indrocraftplugin.utils.SQLUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class Warn implements TabExecutor {

    private final FileConfiguration c = new ConfigUtils(Main.getPlugin(Main.class), "config.yml").getConfig();
    private final SQLUtils sqlUtils = new SQLUtils(c.getString("database.database"),
            c.getString("database.host"),
            c.getString("database.port"),
            c.getString("database.user"),
            c.getString("database.password"));

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {

            //get player and target
            Player player = (Player) sender;
            try {
                Bukkit.getPlayer(args[0]);
            } catch (NullPointerException e) {
                player.sendMessage(ChatColor.RED + "You need to target a real player!");
                return false;
            }

            if (args.length < 2) {
                player.sendMessage("must have a player name followed by a 1, 2 or 3 after that to indicate warn severity!");
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);

            //get current warning
            int warns = sqlUtils.getInt("warns", "UUID", target.getUniqueId().toString(), "players");
            String message = "";
            for (int i = 2; i < args.length; i++) {
                message += " " + args[i];
            }

            int time;
            if (args[1].equals("1"))
                time = warns + 3;
            else if (args[1].equals("2"))
                time = warns + 2;
            else
                time = warns + 1;

            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tempban " + target.getName() + " " + time + "h " + message);

            sqlUtils.setData(String.valueOf(time), "UUID", target.getUniqueId().toString(), "warns", "players");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
