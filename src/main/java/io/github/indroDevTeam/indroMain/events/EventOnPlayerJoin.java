package io.github.indroDevTeam.indroMain.events;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.ranks.Rank;
import io.github.indroDevTeam.indroMain.ranks.RankStorage;
import io.github.indroDevTeam.indroMain.teleports.PointStorage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.UUID;

public class EventOnPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
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
        String format = ChatColor.translateAlternateColorCodes('&', rank.getFormat().replace("%player_name%", player.getName()));
        player.setDisplayName(format);
        player.setPlayerListName(format);

        int seconds = Math.toIntExact(player.getWorld().getTime() / 72);
        LocalTime time = LocalTime.ofSecondOfDay(seconds);
        String[] playerData = new String[]{
            ChatColor.AQUA + "-------------------------",
            ChatColor.AQUA + "| Hello there, " + player.getName(),
            ChatColor.AQUA + "| There are currently " + ChatColor.RED + IndroMain.getInstance().getServer().getOnlinePlayers().size() + ChatColor.AQUA + " player/s online.",
            ChatColor.AQUA + "| Current Rank: " + rank.getRankName(),
            ChatColor.AQUA + "| Home Status: " + PointStorage.findPointsWithOwner(player.getUniqueId().toString()).size() +"/"+ rank.getMaxHomes(),
            ChatColor.AQUA + "| The time is: " + ChatColor.RED + time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
            ChatColor.AQUA + "-------------------------"
        };

        player.sendMessage(playerData);
    }
}
