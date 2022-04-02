package io.github.indroDevTeam.indroMain.ranks;

import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RankUtils {
    public static int criteriaFulfilled(Player player, @Nonnull Rank currentRank) {
        if (currentRank.getAdvancementGate() != null) {
            if (currentRank.getAdvancementGate().isEmpty()) {
                return 0;
            }
            Iterator<Advancement> it = Bukkit.getServer().advancementIterator();
            int achievementsCount = currentRank.getAdvancementGate().size();
            // gets all 'registered' advancements on the server.
            while (it.hasNext()) {
                // loops through these.
                Advancement a = it.next();
                if (currentRank.getAdvancementGate().contains(a.getKey().toString()) &&
                        player.getAdvancementProgress(a).isDone()){
                    achievementsCount--;
                }
            }
            return achievementsCount;
        }
        return 0;
    }

    public static void createRank(String rankName,
                                  String format,
                                  int maxHomes,
                                  @Nullable String nextRank,
                                  @Nullable Integer discordID,
                                  @Nullable String... advancementGates) {
        ArrayList<String> advancementsRequired = null;
        if (advancementGates != null) {
            advancementsRequired = new ArrayList<>(List.of(advancementGates));
        }

        RankStorage.createRank(rankName, format, maxHomes, nextRank, discordID, advancementsRequired);
    }
}
