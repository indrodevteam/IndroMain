package indrocraft.indrocraftplugin.events;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.utils.ConfigUtils;
import indrocraft.indrocraftplugin.utils.SQLUtils;
import indrocraft.indrocraftplugin.utils.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RankEvents implements Listener {

    private final Main main = Main.getPlugin(Main.class);

    private final ConfigUtils c = new ConfigUtils(main, "rank.yml");
    private final ConfigUtils configx = new ConfigUtils(main, "config.yml");
    private final FileConfiguration config = c.getConfig();
    private final RankUtils rankUtils = new RankUtils();
    private final SQLUtils sqlUtils = new SQLUtils(configx.getConfig().getString("database.database"),
            configx.getConfig().getString("database.host"),
            configx.getConfig().getString("database.port"),
            configx.getConfig().getString("database.user"),
            configx.getConfig().getString("database.password"));

    @EventHandler
    public void advancementDoneEvent(PlayerAdvancementDoneEvent event) {

        Player player = event.getPlayer();

        List<String> rankNames = new ArrayList<>();
        List<String> rankAdvancements = new ArrayList<>();
        List<String> nextRanks = new ArrayList<>();

        //fills the 3 lists with all the ranks and their leveling information
        for (String rank : Objects.requireNonNull(config.getConfigurationSection("ranks")).getKeys(false)) {
            rankNames.add(rank);

            String advancement = config.getString("ranks." + rank + ".details.nextAdvancement");
            String nextRank = config.getString("ranks." + rank + ".details.nextRank");

            if (advancement != null) {
                rankAdvancements.add(advancement);
            } else {
                rankAdvancements.add("null");
            }
            if (nextRank != null) {
                nextRanks.add(nextRank);
            } else {
                nextRanks.add("null");
            }
        }

        //gets the players rank and checks what their current target is:
        String playerRank = sqlUtils.getString("rank", "UUID", player.getUniqueId().toString(),
                "players");
        int i = rankNames.indexOf(playerRank);
        String current = rankAdvancements.get(i);

        //if statement, checks if the advancement is the one that the player is currently working towards
        NamespacedKey key = event.getAdvancement().getKey();
        if (key.getNamespace().equals(NamespacedKey.MINECRAFT) && current.equals(key.getKey())) {
            String newRank = nextRanks.get(i);

            //check if the player has completed any of the other ranks after this one
            boolean rankPass = true;
            int rankCount = 0;
            while (rankPass) {
                //get next rank
                int index;
                int reps = rankCount;
                while (reps > 0) {
                    index = rankNames.indexOf(newRank);
                    newRank = nextRanks.get(index);
                    reps--;
                }

                //checks if there is no next rank
                index = rankNames.indexOf(newRank);
                if (index == -1) {
                    player.sendMessage("exiting\n");

                    break;
                }
                String nextAdvancement = rankAdvancements.get(index);

                //is it complete?
                if (rankUtils.hasAdvancement(player, "minecraft:" + nextAdvancement)) {
                    //if so increase rank repeat
                    rankCount++;
                } else {
                    //if not complete or == null break from loop increase rank by 1
                    rankPass = false;
                }
            }

            // these three are used to track the iteration for multiple rank jumps
            String finalRank = playerRank;
            int count = rankCount + 1;
            int finalIndex;
            //used to count the number or ranks a person travels at a time
            int reps = 0;

            while (count > 0) {
                finalIndex = rankNames.indexOf(finalRank);
                finalRank = nextRanks.get(finalIndex);
                reps++;
                count--;
            }

            Bukkit.broadcastMessage(ChatColor.BLUE + "You have gone up " + ChatColor.GREEN + reps + ChatColor.BLUE
                    + " ranks and are now: " + ChatColor.GREEN + finalRank);
            rankUtils.setRank(player, sqlUtils, finalRank);
            rankUtils.LoadRank(player, sqlUtils);
            ServerRankEvent rankEvent = new ServerRankEvent(player, event.getAdvancement(), finalRank);
            Bukkit.getPluginManager().callEvent(rankEvent);

        }
    }
}
