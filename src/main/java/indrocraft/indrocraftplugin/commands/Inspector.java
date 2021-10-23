package indrocraft.indrocraftplugin.commands;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.dataManager.ConfigTools;
import indrocraft.indrocraftplugin.utils.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataHolder;

public class Inspector implements CommandExecutor {

    public Main main;
    public Inspector(Main main) {this.main = main;}

    public FileConfiguration config = ConfigTools.getFileConfig("rank.yml");
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //string gets which rank is designated to be displayed for an inspector
        String inspectorRank = config.getString("inspectorRank");
        if (sender instanceof Player) {
            //checks for the permission
            if (sender.hasPermission("inspector")) {
                Player player = (Player) sender;
                // if their rank is already the one used for inspector set them back to normal
                if (RankUtils.getLevel(player, main.sqlUtils) == Integer.parseInt(inspectorRank)) {
                    player.sendMessage(ChatColor.YELLOW + "Inspector Disabled");
                    RankUtils.setRank(player, main.sqlUtils, player.getMetadata("oldRank").get(0).asString());
                } else {
                    // else set their rank to inspector
                    PersistentDataHolder holder = (PersistentDataHolder) player;

                    Bukkit.dispatchCommand(player, "say hello");
                    player.sendMessage(ChatColor.YELLOW + "Inspector Enabled");
                    String lastRank = String.valueOf(RankUtils.getLevel(player, main.sqlUtils));
                    player.setMetadata("oldRank", new FixedMetadataValue(this.main, lastRank));
                    RankUtils.setRank(player, main.sqlUtils, inspectorRank);
                }
                RankUtils.LoadRank(player, main.sqlUtils);
            }
        }
        return true;
    }
}
