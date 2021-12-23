package indrocraft.indrocraftplugin.events;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.dataManager.ConfigTools;
import indrocraft.indrocraftplugin.utils.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
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

        NamespacedKey key = event.getAdvancement().getKey();
        if (key.getNamespace().equals(NamespacedKey.MINECRAFT) && rankAdvancements.contains(key.getKey())) {
            int index = rankAdvancements.indexOf(key.getKey());
            String next = nextRanks.get(index);

            boolean rankPass = true;
            while (rankPass) {
                int index2 = rankNames.indexOf(next);
                String nextAdvancement = rankAdvancements.get(index2);

                NamespacedKey nsk = new NamespacedKey(NamespacedKey.MINECRAFT, nextAdvancement);
                Advancement advancement = main.getServer().getAdvancement(nsk);

                player.sendMessage("i");

                if (advancement == null) {
                    rankPass = false;
                }

                if (player.getAdvancementProgress(advancement).isDone()) {

                    int index3 = rankAdvancements.indexOf(nextAdvancement);
                    String next2 = nextRanks.get(index3);
                    player.sendMessage(next2 + " this is the rank");
                    rankUtils.setRank(player, main.sqlUtils, next2);
                    rankUtils.LoadRank(player, main.sqlUtils);

                    player.sendMessage("Ranked up again!");
                    rankPass = false;
                } else {
                    rankUtils.setRank(player, main.sqlUtils, next);
                    rankUtils.LoadRank(player, main.sqlUtils);
                    player.sendMessage("congratulations you are now rank: " + nextRanks.get(index));
                    rankPass = false;
                }
            }
        }
    }
}
