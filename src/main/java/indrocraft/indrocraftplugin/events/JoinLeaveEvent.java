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

    private final Main main = Main.getPlugin(Main.class);

    ConfigTools config = new ConfigTools(main, "config.yml");
    ConfigTools ranksConfig = new ConfigTools(main, "rank.yml");

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

        main.sqlUtils.createRow("UUID", uuid, "players");

        String ign = main.sqlUtils.getString("ign", "UUID", uuid, "players");
        if (ign != name) {
            main.sqlUtils.setData(name, "UUID", uuid, "ign", "players");
        }
        String warns = main.sqlUtils.getString("ign", "UUID", uuid, "players");
        if (warns == null) {
            main.sqlUtils.setData("0", "UUID", uuid, "warns", "players");
        }

        //columns for homes:
        if (config.getConfig().getBoolean("homes")) {
            String tpDatabase = config.getConfig().getString("databaseForTP");
            //players columns:
            main.sqlUtils.createColumn(tpDatabase, "VARCHAR(100)", "players");
            main.sqlUtils.createColumn(tpDatabase + "num", "INT(100)", "players");
            //location Storage:
            main.sqlUtils.createTable(tpDatabase, "homeID");
            main.sqlUtils.createColumn("playerID", "VARCHAR(100)", tpDatabase);
            main.sqlUtils.createColumn("world", "VARCHAR(200)", tpDatabase);
            main.sqlUtils.createColumn("x", "DOUBLE", tpDatabase);
            main.sqlUtils.createColumn("y", "DOUBLE", tpDatabase);
            main.sqlUtils.createColumn("z", "DOUBLE", tpDatabase);
            main.sqlUtils.createColumn("pitch", "Float", tpDatabase);
            main.sqlUtils.createColumn("yaw", "Float", tpDatabase);

            String homeList = main.sqlUtils.getString(tpDatabase, "UUID", uuid, "players");
            if (homeList == null) {
                main.sqlUtils.setData(" ", "UUID", uuid, tpDatabase, "players");
            }
            String homeNum = main.sqlUtils.getString(tpDatabase + "num", "UUID", uuid,
                    "players");
            if (homeNum == null) {
                main.sqlUtils.setData("0", "UUID", uuid, tpDatabase + "num", "players");
            }
        }

        //load rank:
        if (ranksConfig.getConfig().getBoolean("useRanks")) {
            main.sqlUtils.createColumn("rank", "int(100)", "players");
            main.sqlUtils.createColumn("nameColour", "VARCHAR(100)", "players");

            String rank = main.sqlUtils.getString("rank", "UUID", uuid, "players");
            if (rank == null) {
                main.sqlUtils.setData("0", "UUID", uuid, "rank", "players");
            }
            String nameColour = main.sqlUtils.getString("nameColour", "UUID", uuid, "players");
            if (nameColour == null) {
                main.sqlUtils.setData("WHITE", "UUID", uuid, "nameColour", "players");
            }

            RankUtils.LoadRank(player, main.sqlUtils);
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
