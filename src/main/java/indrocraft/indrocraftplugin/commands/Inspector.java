package indrocraft.indrocraftplugin.commands;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.dataManager.ConfigTools;
import indrocraft.indrocraftplugin.utils.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataHolder;

public class Inspector implements CommandExecutor {

    private Main main = Main.getPlugin(Main.class);

    ConfigTools config = new ConfigTools(main, "config.yml");
    private RankUtils rankUtils = new RankUtils();
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //string gets which rank is designated to be displayed for an inspector
        String inspectorRank = config.getConfig().getString("inspectorRank");
        if (sender instanceof Player) {
            //checks for the permission
            if (sender.hasPermission("inspector")) {
                Player player = (Player) sender;
                // if their rank is already the one used for inspector set them back to normal
                if (rankUtils.getLevel(player, main.sqlUtils) == Integer.parseInt(inspectorRank)) {
                    player.sendMessage(ChatColor.YELLOW + "Inspector Disabled");
                    rankUtils.setRank(player, main.sqlUtils, player.getMetadata("oldRank").get(0).asString());
                } else {
                    // else set their rank to inspector
                    PersistentDataHolder holder = (PersistentDataHolder) player;

                    Bukkit.dispatchCommand(player, "say hello");
                    player.sendMessage(ChatColor.YELLOW + "Inspector Enabled");
                    String lastRank = String.valueOf(rankUtils.getLevel(player, main.sqlUtils));
                    player.setMetadata("oldRank", new FixedMetadataValue(this.main, lastRank));
                    rankUtils.setRank(player, main.sqlUtils, inspectorRank);
                }
                rankUtils.LoadRank(player, main.sqlUtils);
            }
        }
        return true;
    }
}
