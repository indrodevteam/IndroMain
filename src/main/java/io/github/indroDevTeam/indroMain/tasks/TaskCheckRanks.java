package io.github.indroDevTeam.indroMain.tasks;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.dataUtils.LanguageTags;
import io.github.indroDevTeam.indroMain.events.EventOnRankUp;
import io.github.indroDevTeam.indroMain.ranks.Rank;
import io.github.indroDevTeam.indroMain.ranks.RankStorage;
import io.github.indroDevTeam.indroMain.ranks.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class TaskCheckRanks {
    public static void run() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(IndroMain.getInstance(), () -> {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                HashMap<UUID, Rank> rankHashMap = IndroMain.getPlayerRankList();
                Rank rank;
                if (rankHashMap.containsKey(player.getUniqueId())) {
                    rank = rankHashMap.get(player.getUniqueId());
                } else { // means the player didn't save properly, I don't know
                    rank = RankStorage.readRank("DEFAULT");
                    rankHashMap.put(player.getUniqueId(), rank);
                    IndroMain.setPlayerRankList(rankHashMap);
                }

                assert rank != null;

                // updates names
                String format = ChatColor.translateAlternateColorCodes('&', rank.getFormat().replace("%player_name%", player.getName()));
                player.setDisplayName(format);
                player.setPlayerListName(format);

                // check if they are in the next rank
                Rank nextRank = RankStorage.readRank(rank.getNextRank());

                if (nextRank == null) { // means that player has reached the last rank
                    continue;
                }

                boolean criteriaRemain = RankUtils.criteriaFulfilled(player, nextRank) == 0;
                if (criteriaRemain) {
                    String nextRankName = rank.getNextRank();
                    Rank newRank = RankStorage.readRank(nextRankName);
                    rankHashMap.replace(player.getUniqueId(), rank, newRank);

                    // updates current storage of ranks
                    rank = rankHashMap.get(player.getUniqueId());
                    player.sendMessage(LanguageTags.RANK_UP.get().replace("%rank_name%", rank.getRankName().toLowerCase(Locale.ROOT)));
                    EventOnRankUp event = new EventOnRankUp(player, rank, newRank);
                    Bukkit.getPluginManager().callEvent(event);
                }
            }
        }, 0, 40);
    }
}
