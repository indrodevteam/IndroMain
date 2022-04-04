package io.github.indroDevTeam.indroMain.tasks;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.dataUtils.LanguageTags;
import io.github.indroDevTeam.indroMain.events.EventOnRankPromote;
import io.github.indroDevTeam.indroMain.ranks.Rank;
import io.github.indroDevTeam.indroMain.ranks.RankStorage;
import io.github.indroDevTeam.indroMain.ranks.RankUtils;
import io.github.indroDevTeam.indroMain.ranks.UserRanks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.*;

public class TaskCheckRanks {
    public static void run() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(IndroMain.getInstance(), () -> {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                Rank rank = UserRanks.getRank(player);

                ChatColor meta = UserRanks.getChatColor(player);

                // updates names
                String format = RankUtils.translate(rank.getFormat().replace("%player_name%", meta + player.getName()));
                player.setDisplayName(format);
                player.setPlayerListName(format);
            }
        }, 0, 40);
    }
}
