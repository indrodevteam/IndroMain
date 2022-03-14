package io.github.indroDevTeam.indroMain.tasks;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.dataUtils.LanguageLoader;
import io.github.indroDevTeam.indroMain.events.EventOnRankUp;
import io.github.indroDevTeam.indroMain.ranks.Rank;
import io.github.indroDevTeam.indroMain.ranks.RankStorage;
import io.github.indroDevTeam.indroMain.ranks.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;

import java.util.*;

public class TaskCheckRanks {
    private final List<Player> playerList = new ArrayList<>();

    public TaskCheckRanks() {
        playerList.addAll(Bukkit.getServer().getOnlinePlayers());
    }

    public void run() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(IndroMain.getInstance(), () -> {
            Bukkit.getLogger().warning("3");
            for (Player player : playerList) {
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

                boolean criteriaRemain = RankUtils.criteriaFulfilled(player, rank);
                if (criteriaRemain) {
                    String nextRank = rank.getNextRank();
                    Rank newRank = RankStorage.readRank(nextRank);
                    rankHashMap.replace(player.getUniqueId(), rank, newRank);

                    // updates current storage of ranks
                    rank = rankHashMap.get(player.getUniqueId());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    new LanguageLoader().get("plugin-title") +
                                    ChatColor.translateAlternateColorCodes('&', new LanguageLoader().get("rank-up").replace("%rank_name%", rank.getRankName().toLowerCase(Locale.ROOT)))));
                    EventOnRankUp event = new EventOnRankUp(player, rank, newRank);
                    Bukkit.getPluginManager().callEvent(event);
                }

                // updates names
                String format = ChatColor.translateAlternateColorCodes('&', rank.getFormat().replace("%player_name%", player.getName()));
                player.setDisplayName(format);
                player.setPlayerListName(format);
            }
        }, 0, 40);
    }
}
