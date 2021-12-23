package indrocraft.indrocraftplugin;

import indrocraft.indrocraftplugin.commands.*;
import indrocraft.indrocraftplugin.dataManager.ConfigTools;
import indrocraft.indrocraftplugin.dataManager.MySQL;
import indrocraft.indrocraftplugin.utils.SQLUtils;
import indrocraft.indrocraftplugin.events.JoinLeaveEvent;
import indrocraft.indrocraftplugin.events.RankEvents;
import indrocraft.indrocraftplugin.utils.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;


public final class Main extends JavaPlugin{

    public MySQL SQL;
    public SQLUtils sqlUtils;
    public RankUtils rankUtils;

    @Override
    public void onEnable() {
        // Plugin startup logic

        //initialize config files
        ConfigTools config = new ConfigTools(this, "config.yml");
        config.saveDefaultConfig();
        ConfigTools ranks = new ConfigTools(this, "rank.yml");
        ranks.saveDefaultConfig();
        ConfigTools warps = new ConfigTools(this, "warps.yml");
        warps.saveDefaultConfig();

        //init utils
        sqlUtils = new SQLUtils(this);
        SQL = new MySQL();
        rankUtils = new RankUtils();

        // commands:
        getServer().getPluginCommand("dev").setExecutor(new Dev());
        getServer().getPluginCommand("warn").setExecutor(new Warn());
        getServer().getPluginCommand("home").setExecutor(new Home());
        getServer().getPluginCommand("warp").setExecutor(new Warp());
        getServer().getPluginCommand("ranks").setExecutor(new RankCommand());

        // tab executors:
        getCommand("warn").setTabCompleter(new Warn());
        getCommand("home").setTabCompleter(new Home());
        getCommand("ranks").setTabCompleter(new RankCommand());

        // connects to the database:
        //this.SQL = new MySQL(this);

        try {
            SQL.connect();
        } catch (ClassNotFoundException | SQLException e) {
            Bukkit.getLogger().severe("Database not connected!");
        }

        if (SQL.isConnected()) {
            Bukkit.getLogger().info(ChatColor.BLUE + "Database is connected!");
        }

        //register events:
        getServer().getPluginManager().registerEvents(new JoinLeaveEvent(), this);
        getServer().getPluginManager().registerEvents(new RankEvents(), this);

        try {
            sqlUtils.createTable("players", "UUID");
            sqlUtils.createColumn("ign", "VARCHAR(100)", "players");
            sqlUtils.createColumn("warns", "INT", "players");
            if (config.getConfig().getBoolean("useWarp")) {
                String database = config.getConfig().getString("databaseForTP") + "Warps";
                sqlUtils.createTable(database, "warpID");
                sqlUtils.createColumn("x", "DOUBLE", database);
                sqlUtils.createColumn("y", "DOUBLE", database);
                sqlUtils.createColumn("z", "DOUBLE", database);
                sqlUtils.createColumn("world", "VARCHAR(100)", database);
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("startup table creation canceled please connect database");
        }
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SQL.disconnect();
        Bukkit.getLogger().info("Successfully disabled Indrocraft plugin!");
    }
}