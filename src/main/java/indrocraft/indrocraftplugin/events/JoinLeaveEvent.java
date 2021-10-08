package indrocraft.indrocraftplugin.events;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.dataManager.ConfigTools;
import indrocraft.indrocraftplugin.utils.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveEvent implements Listener {

    public FileConfiguration config = ConfigTools.getFileConfig("rank.yml");

    public Main main;
    public JoinLeaveEvent(Main main) {this.main = main;}

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        //join message:
        String serverName = config.getString("serverName");
        event.setJoinMessage(ChatColor.GREEN  + "Welcome " + ChatColor.YELLOW + player.getName() + ChatColor.GREEN +
                " To " + ChatColor.BOLD + serverName + "!");

        //fill in table players:
        String uuid = player.getUniqueId().toString();
        String name = player.getDisplayName();

        main.sqlUtils.createRow("UUID", uuid, "players");

        String ign = main.sqlUtils.getString("ign", "UUID", uuid, "players");
        if (ign != name) {
            main.sqlUtils.setData(name, "UUID", uuid, "ign", "players");
        }

        //load rank:
        if (config.getBoolean("useRanks")) {
            main.sqlUtils.createColumn("rank", "int(100)", "players");
            main.sqlUtils.createColumn("nameColour", "VARCHAR(100)", "players");

            String rank = main.sqlUtils.getString("rank", "UUID", uuid, "players");
            if (rank == null) {
                main.sqlUtils.setData("0", "UUID", uuid, "rank", "players");
            }
            String nameColour = main.sqlUtils.getString("nameColour", "UUID", uuid, "players");
            if (rank == null) {
                main.sqlUtils.setData("WHITE", "UUID", uuid, "nameColour", "players");
            }

            main.rankUtils.LoadRank(player, main.sqlUtils);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(ChatColor.GREEN + "See you soon " + ChatColor.YELLOW + player.getName() +
                ChatColor.GREEN + "!");
    }

    @EventHandler
    public void onPlayerSleep(PlayerBedLeaveEvent event) {
        Player p = event.getPlayer();

        Long time = p.getWorld().getTime();
        if (time <= 50) {
            p.sendMessage(time.toString());
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatColor.YELLOW + p.getName() + ChatColor.GREEN + " went to bed sweet dreams!");
            }
        }

    }
}
