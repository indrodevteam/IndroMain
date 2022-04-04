package io.github.indroDevTeam.indroMain.events;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.ranks.Rank;
import io.github.indroDevTeam.indroMain.ranks.RankStorage;
import io.github.indroDevTeam.indroMain.ranks.UserRanks;
import io.github.indroDevTeam.indroMain.teleports.PointStorage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.UUID;

public class EventOnPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Rank rank = UserRanks.getRank(player);

        String format = ChatColor.translateAlternateColorCodes('&', rank.getFormat().replace("%player_name%", player.getName()));
        player.setDisplayName(format);
        player.setPlayerListName(format);

        String[] playerData = new String[]{
            ChatColor.AQUA + "-------------------------",
            ChatColor.AQUA + "| Hello there, " + player.getName(),
            ChatColor.AQUA + "| There are currently " + ChatColor.RED + IndroMain.getInstance().getServer().getOnlinePlayers().size() + ChatColor.AQUA + " player/s online.",
            ChatColor.AQUA + "| Current Rank: " + rank.getRankTag(),
            ChatColor.AQUA + "| Home Count: " + PointStorage.findPointsWithOwner(player.getUniqueId().toString()).size() +"/"+ rank.getMaxHomes(),
            ChatColor.AQUA + "-------------------------"
        };

        player.sendMessage(playerData);
    }
}
