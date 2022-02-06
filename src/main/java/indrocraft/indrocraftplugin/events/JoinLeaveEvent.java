package indrocraft.indrocraftplugin.events;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.utils.ConfigUtils;
import indrocraft.indrocraftplugin.utils.SQLUtils;
import indrocraft.indrocraftplugin.utils.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class JoinLeaveEvent implements Listener {

    private final Main main = Main.getPlugin(Main.class);

    ConfigUtils config = new ConfigUtils(main, "config.yml");
    ConfigUtils ranksConfig = new ConfigUtils(main, "rank.yml");
    RankUtils rankUtils = new RankUtils();
    SQLUtils sqlUtils = new SQLUtils(main.sqlconnector);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        //join message:
        String serverName = config.getConfig().getString("serverName");
        event.setJoinMessage(ChatColor.GREEN  + "Welcome " + ChatColor.YELLOW + player.getName() + ChatColor.GREEN +
                " To " + ChatColor.BOLD + serverName + "!");

        //fill in table players:
        String uuid = player.getUniqueId().toString();
        String name = player.getDisplayName();

        sqlUtils.createRow("UUID", uuid, "players");

        String ign = sqlUtils.getString("ign", "UUID", uuid, "players");
        if (ign != name) {
            sqlUtils.setData(name, "UUID", uuid, "ign", "players");
        }
        String warns = sqlUtils.getString("ign", "UUID", uuid, "players");
        if (warns == null) {
            sqlUtils.setData("0", "UUID", uuid, "warns", "players");
        }

        //columns for homes:
        if (config.getConfig().getBoolean("homes")) {
            String tpDatabase = config.getConfig().getString("databaseForTP");
            //players columns:
            sqlUtils.createColumn(tpDatabase, "VARCHAR(100)", "players");
            sqlUtils.createColumn(tpDatabase + "num", "INT(100)", "players");
            //location Storage:
            sqlUtils.createTable(tpDatabase, "homeID");
            sqlUtils.createColumn("playerID", "VARCHAR(100)", tpDatabase);
            sqlUtils.createColumn("world", "VARCHAR(200)", tpDatabase);
            sqlUtils.createColumn("x", "DOUBLE", tpDatabase);
            sqlUtils.createColumn("y", "DOUBLE", tpDatabase);
            sqlUtils.createColumn("z", "DOUBLE", tpDatabase);
            sqlUtils.createColumn("pitch", "Float", tpDatabase);
            sqlUtils.createColumn("yaw", "Float", tpDatabase);

            String homeList = sqlUtils.getString(tpDatabase, "UUID", uuid, "players");
            if (homeList == null || homeList.isEmpty()) {
                sqlUtils.setData(" ", "UUID", uuid, tpDatabase, "players");
            }
            String homeNum = sqlUtils.getString(tpDatabase + "num", "UUID", uuid,
                    "players");
            if (homeNum == null) {
                sqlUtils.setData("0", "UUID", uuid, tpDatabase + "num", "players");
            }
        }

        //load rank:
        if (ranksConfig.getConfig().getBoolean("useRanks")) {
            sqlUtils.createColumn("rank", "VARCHAR(100)", "players");
            sqlUtils.createColumn("nameColour", "VARCHAR(100)", "players");

            List<String> defaultRank = new ArrayList<>(
                    ranksConfig.getConfig().getConfigurationSection("ranks").getKeys(false));

            String rank = sqlUtils.getString("rank", "UUID", uuid, "players");
            if (rank == null || rank.isEmpty()) {
                sqlUtils.setData(defaultRank.get(0), "UUID", uuid, "rank", "players");
            }
            String nameColour = sqlUtils.getString("nameColour", "UUID", uuid, "players");
            if (rank == null || nameColour.isEmpty()) {
                sqlUtils.setData("WHITE", "UUID", uuid, "nameColour", "players");
            }

            rankUtils.LoadRank(player, sqlUtils);
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
