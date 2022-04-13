package com.github.indrodevteam.indroMain.events;

import com.github.indrodevteam.indroMain.ranks.Rank;
import com.github.indrodevteam.indroMain.ranks.UserRanks;
import com.github.indrodevteam.indroMain.teleports.PointStorage;
import com.github.indrodevteam.indroMain.IndroMain;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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