package indrocraft.indrocraftplugin.commands;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.utils.ConfigUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Dev implements CommandExecutor{

    private final Main main = Main.getPlugin(Main.class);

    private final ConfigUtils c = new ConfigUtils(main, "rank.yml");
    private FileConfiguration config = c.getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player =(Player) sender;

        List<String> rankAdvancements = new ArrayList<>();
        List<String> nextRanks = new ArrayList<>();

        for (String rank : config.getConfigurationSection("ranks").getKeys(false)) {
            player.sendMessage(rank);
            String advancement = config.getString("ranks." + rank + ".details.nextAdvancement");
            String nextRank = config.getString("ranks." + rank + ".details.nextRank");

            if (advancement == null) {
                player.sendMessage("nothing set for next advance");
            } else {
                player.sendMessage(advancement);
                rankAdvancements.add(advancement);
            }

            if (nextRank == null) {
                player.sendMessage("nothing set for next rank");
            } else {
                player.sendMessage(nextRank);
                nextRanks.add(nextRank);
            }

        }

        return true;
    }
}
