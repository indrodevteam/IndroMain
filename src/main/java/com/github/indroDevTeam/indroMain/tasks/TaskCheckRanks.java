package com.github.indroDevTeam.indroMain.tasks;

import com.github.indroDevTeam.indroMain.IndroMain;
import com.github.indroDevTeam.indroMain.ranks.Rank;
import com.github.indroDevTeam.indroMain.ranks.RankUtils;
import com.github.indroDevTeam.indroMain.ranks.UserRanks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
