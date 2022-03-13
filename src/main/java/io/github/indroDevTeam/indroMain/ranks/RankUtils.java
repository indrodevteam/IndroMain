package io.github.indroDevTeam.indroMain.ranks;

import io.github.indroDevTeam.indroMain.dataUtils.LanguageLoader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class RankUtils {
    public static boolean criteriaFulfilled(Player player, Rank currentRank) {
        if (currentRank.getNextAdvancement() != null) {
            Iterator<Advancement> it = Bukkit.getServer().advancementIterator();
            // gets all 'registered' advancements on the server.
            while (it.hasNext()) {
                // loops through these.
                Advancement a = it.next();
                if (a.getKey().toString().equalsIgnoreCase(currentRank.getNextAdvancement())) {
                    return player.getAdvancementProgress(a).isDone();
                }
            }
        }
        return false;
    }


}
