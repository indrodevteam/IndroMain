package indrocraft.indrocraftplugin.events;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.dataManager.ConfigTools;
import indrocraft.indrocraftplugin.utils.RankUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import java.util.ArrayList;
import java.util.List;

public class RankEvents implements Listener {

    private Main main = Main.getPlugin(Main.class);

    public ConfigTools config = new ConfigTools(main, "ranks.yml");
    private RankUtils rankUtils = new RankUtils();

    @EventHandler
    public void advancementDoneEvent(PlayerAdvancementDoneEvent event) {

        Player player = event.getPlayer();

        if (config.getConfig().getBoolean("autoRankUp")) {
            NamespacedKey key = event.getAdvancement().getKey();
            Integer level = rankUtils.getLevel(player, main.sqlUtils);
            List<String> rankUpOrder = new ArrayList<>();
            List<String> totalRanks = new ArrayList<>();
            for (String ranks : config.getConfig().getConfigurationSection("ranks").getKeys(false))
                totalRanks.add(ranks);

            for (Integer i = 0; i<totalRanks.size(); i++) {
                String na = config.getConfig().getString("ranks." + i + ".advancements");
                rankUpOrder.add(na);
            }
            player.sendMessage(rankUpOrder.toString());

            if (key.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                if (rankUpOrder.contains(key.getKey())) {

                    Integer advancement = rankUpOrder.indexOf(key.getKey());
                    if (key.getKey().equals(rankUpOrder.get(advancement))) {
                        if (level < advancement + 1) {
                            level++;

                            advancement++;
                            rankUtils.levelUp(player, main.sqlUtils, advancement.toString());
                        }
                    }
                }
            }
        }
    }
}
