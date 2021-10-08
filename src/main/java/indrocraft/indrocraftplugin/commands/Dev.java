package indrocraft.indrocraftplugin.commands;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.dataManager.ConfigTools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class Dev implements CommandExecutor {

    private Main main;
    public Dev(Main main) {this.main = main;}

    //public FileConfiguration config = getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //Player player = (Player) sender;
        FileConfiguration config = ConfigTools.getFileConfig("config.yml");

        if (sender.isOp()) {
            if (label.equalsIgnoreCase("double")) {
                Double number = main.sqlUtils.getDouble("test", "NAME", "player", "testing");
                Bukkit.getLogger().warning(number.toString());
            }
            if (label.equalsIgnoreCase("float")) {
                Float number = main.sqlUtils.getFloat("test", "NAME", "player", "testing");
                Bukkit.getLogger().warning(number.toString());
            }
            if (label.equalsIgnoreCase("int")) {
                Integer number = main.sqlUtils.getInt("test", "NAME", "player", "testing");
                Bukkit.getLogger().warning(number.toString());
            }
            if (label.equalsIgnoreCase("string")) {
                String number = main.sqlUtils.getString("test", "NAME", "player", "testing");
                Bukkit.getLogger().warning(number.toString());
            }
            if (label.equalsIgnoreCase("dev")) {
                main.sqlUtils.setData(args[0], "NAME", "player", "test", "testing");
            }
        }


        return false;
    }
}
