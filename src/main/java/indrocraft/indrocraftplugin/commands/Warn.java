package indrocraft.indrocraftplugin.commands;

import indrocraft.indrocraftplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class Warn implements TabExecutor {

    private Main main = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            Player player = (Player) sender;
            try {
                Bukkit.getPlayer(args[0]);
            } catch (NullPointerException e) {
                player.sendMessage(ChatColor.RED + "You need to target a real player!");
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);

            Integer warns = main.sqlUtils.getInt("warns", "UUID", target.getUniqueId().toString(), "players");
            if (warns == null) {
                warns = 1;
            } else {
                warns++;
            }

            Integer time = warns*3-3;
            String message = "";
            for (int i = 1; i < args.length; i++) {
                message += " " + args[i];
            }
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tempban " + target.getName() + time + message);

            main.sqlUtils.setData(warns.toString(), "UUID", target.getUniqueId().toString(), "warns", "players");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
