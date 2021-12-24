package indrocraft.indrocraftplugin.events;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.dataManager.ConfigTools;
import indrocraft.indrocraftplugin.utils.RankUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import java.util.ArrayList;
import java.util.List;

public class RankEvents implements Listener {

    private final Main main = Main.getPlugin(Main.class);

    private final ConfigTools c = new ConfigTools(main, "rank.yml");
    private final FileConfiguration config = c.getConfig();
    private final RankUtils rankUtils = new RankUtils();

    @EventHandler
    public void advancementDoneEvent(PlayerAdvancementDoneEvent event) {

        Player player = event.getPlayer();

        List<String> rankNames = new ArrayList<>();
        List<String> rankAdvancements = new ArrayList<>();
        List<String> nextRanks = new ArrayList<>();

        //fills the 3 lists with all the ranks and their leveling information
        for (String rank : config.getConfigurationSection("ranks").getKeys(false)) {
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
        String playerRank = main.sqlUtils.getString("rank", "UUID", player.getUniqueId().toString(),
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

                String nextRank = null;
                int index = 0;

                while (rankCount > 0) {
                    index = rankNames.indexOf(newRank);
                    nextRank = nextRanks.get(index);
                    rankCount--;
                }

                index = rankNames.indexOf(nextRank);
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
            player.sendMessage(String.valueOf(rankCount));
            rankUtils.LoadRank(player, main.sqlUtils);

        }
    }


}
