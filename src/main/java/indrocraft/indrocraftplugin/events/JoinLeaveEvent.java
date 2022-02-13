package indrocraft.indrocraftplugin.events;

import indrocraft.indrocraftplugin.Config;
import indrocraft.indrocraftplugin.Main;
import io.github.indroDevTeam.indroLib.datamanager.SQLUtils;
import io.github.indroDevTeam.indroLib.objects.ranks.Rank;
import io.github.indroDevTeam.indroLib.objects.ranks.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.Objects;

public class JoinLeaveEvent implements Listener {

    private final Main main = Main.getPlugin(Main.class);

    private final Config CONFIG = new Config(main);
    private final SQLUtils sqlUtils = new SQLUtils(main.sqlconnector);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        //join message:
        String serverName = CONFIG.getServerName();
        event.setJoinMessage(
                ChatColor.GREEN + "Welcome " + ChatColor.YELLOW + player.getName() + ChatColor.GREEN + " To "
                        + ChatColor.BOLD + serverName + "!"
        );

        //fill in table players:
        String uuid = player.getUniqueId().toString();
        String name = player.getDisplayName();

        sqlUtils.createRow("UUID", uuid, "players");

        String ign = sqlUtils.getString("ign", "UUID", uuid, "players");
        if (!Objects.equals(ign, name)) {
            sqlUtils.setData(name, "UUID", uuid, "ign", "players");
        }
        String warns = sqlUtils.getString("ign", "UUID", uuid, "players");
        if (warns == null) {
            sqlUtils.setData("0", "UUID", uuid, "warns", "players");
        }

        //load rank:
        if (CONFIG.isRanks()) {
            List<Rank> defaultRank = RankUtils.getRanks(sqlUtils);

            String rank = (String) sqlUtils.getData("rank", "UUID", uuid, "players");
            if (rank == null || rank.isEmpty()) {
                sqlUtils.setData(defaultRank.get(0).getId(), "UUID", uuid, "rank", "players");
            }
            String nameColour = (String) sqlUtils.getData("nameColour", "UUID", uuid, "players");
            if (nameColour == null || nameColour.isEmpty()) {
                sqlUtils.setData("WHITE", "UUID", uuid, "nameColour", "players");
            }

            RankUtils.loadPlayerRank(player, sqlUtils);
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

        long time = p.getWorld().getTime();
        if (time <= 50) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatColor.YELLOW + p.getName() + ChatColor.GREEN + " went to bed sweet dreams!");
            }
        }
    }
}
