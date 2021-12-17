package indrocraft.indrocraftplugin;

import indrocraft.indrocraftplugin.commands.*;
import indrocraft.indrocraftplugin.dataManager.ConfigTools;
import indrocraft.indrocraftplugin.dataManager.SQLite;
import indrocraft.indrocraftplugin.utils.SQLUtils;
import indrocraft.indrocraftplugin.events.JoinLeaveEvent;
import indrocraft.indrocraftplugin.events.RankEvents;
import indrocraft.indrocraftplugin.utils.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;


public final class Main extends JavaPlugin{

    public SQLUtils sqlUtils;
    public RankUtils rankUtils;
    public SQLite sqLite;

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
        rankUtils = new RankUtils();

        //initialize and load db
        sqLite = new SQLite(this);
        sqLite.load();
        Connection connection = sqLite.getSQLConnection();
        sqlUtils = new SQLUtils(connection);

        sqlUtils.createTable("players", "UUID");
        sqlUtils.createRow("UUID", "test", "players");
        String test = sqlUtils.getString("UUID", "UUID", "test", "players");
        Bukkit.getLogger().info(test);

        // commands:
        getServer().getPluginCommand("dev").setExecutor(new Dev());
        getServer().getPluginCommand("setRank").setExecutor(new SetRank());
        getServer().getPluginCommand("warn").setExecutor(new Warn());
        getServer().getPluginCommand("home").setExecutor(new Home());
        getServer().getPluginCommand("inspector").setExecutor(new Inspector());
        getServer().getPluginCommand("warp").setExecutor(new Warp());
        getServer().getPluginCommand("rankEditor").setExecutor(new RankEditor());

        // tab executors:
        getCommand("warn").setTabCompleter(new Warn());
        getCommand("home").setTabCompleter(new Home());
        getCommand("rankEditor").setTabCompleter(new RankEditor());

        //register events:
        getServer().getPluginManager().registerEvents(new JoinLeaveEvent(), this);
        getServer().getPluginManager().registerEvents(new RankEvents(), this);

        /*try {
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
        }*/
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("Successfully disabled Indrocraft plugin!");
    }
}