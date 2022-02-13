package indrocraft.indrocraftplugin;

import indrocraft.indrocraftplugin.commands.HomeCommand;
import indrocraft.indrocraftplugin.commands.RankCommand;
import indrocraft.indrocraftplugin.commands.WarnCommand;
import indrocraft.indrocraftplugin.commands.WarpCommand;
import indrocraft.indrocraftplugin.events.JoinLeaveEvent;
import indrocraft.indrocraftplugin.events.RankEvents;
import io.github.indroDevTeam.indroLib.MiscUtils;
import io.github.indroDevTeam.indroLib.datamanager.ConfigUtils;
import io.github.indroDevTeam.indroLib.datamanager.SQLConnector;
import io.github.indroDevTeam.indroLib.datamanager.SQLUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public SQLConnector sqlconnector;

    @Override
    public void onEnable() {
        //initialize Config files
        ConfigUtils config = new ConfigUtils(this, "config.yml");
        config.saveDefaultConfig();

        Config CONFIG = new Config(this);

        //connect to database
        sqlconnector = new SQLConnector(
                CONFIG.getDatabase(),
                CONFIG.getHost(),
                CONFIG.getPort(),
                CONFIG.getUsername(),
                CONFIG.getPassword(),
                CONFIG.isSQLite(),
                this
        );

        SQLUtils s = new SQLUtils(sqlconnector);
        MiscUtils.setupDatabase(s);

        // commands:
        getServer().getPluginCommand("warn").setExecutor(new WarnCommand());
        getServer().getPluginCommand("home").setExecutor(new HomeCommand());
        getServer().getPluginCommand("warp").setExecutor(new WarpCommand());
        getServer().getPluginCommand("ranks").setExecutor(new RankCommand());
        getServer().getPluginCommand("config").setExecutor(new Config(this));

        // tab executors:
        getCommand("warn").setTabCompleter(new WarnCommand());
        getCommand("home").setTabCompleter(new HomeCommand());
        getCommand("ranks").setTabCompleter(new RankCommand());

        //register events:
        getServer().getPluginManager().registerEvents(new JoinLeaveEvent(), this);
        getServer().getPluginManager().registerEvents(new RankEvents(), this);

        //connection message:
        String dbType = ChatColor.BLUE + "MySQL " + ChatColor.WHITE;
        String dbStatus = ChatColor.RED + "Database not connected!" + ChatColor.WHITE;
        String isWarps = ChatColor.RED + "false" + ChatColor.WHITE;
        String isHomes = ChatColor.RED + "false" + ChatColor.WHITE;
        String isRanks = ChatColor.RED + "false" + ChatColor.WHITE;
        if (sqlconnector.isUseSQLite()) dbType = ChatColor.BLUE + "SQLite" + ChatColor.WHITE;
        if (CONFIG.isWarps()) isWarps = ChatColor.GREEN + "true " + ChatColor.WHITE;
        if (CONFIG.isHomes()) isHomes = ChatColor.GREEN + "true " + ChatColor.WHITE;
        if (CONFIG.isRanks()) isRanks = ChatColor.GREEN + "true " + ChatColor.WHITE;
        if (sqlconnector.isReady())
            dbStatus = ChatColor.GREEN + "Database Ready!        " + ChatColor.WHITE;

        Bukkit.getLogger().info(
                "Indrocraft startup info: " + ChatColor.WHITE +
                "\n[]-------[Indrocraft Plugin]-------[]\n" +
                "| Database: " + dbStatus + " |\n" +
                "| DatabaseType: " + dbType + "              |\n" +
                "| Using Ranks: " + isRanks + "                |\n" +
                "| Using Homes: " + isHomes + "                |\n" +
                "| Using Warps: " + isWarps + "                |\n" +
                "[]---------------------------------[]"
        );
    }


    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Successfully disabled Indrocraft plugin!");
    }
}