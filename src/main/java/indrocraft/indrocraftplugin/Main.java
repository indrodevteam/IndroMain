package indrocraft.indrocraftplugin;

import indrocraft.indrocraftplugin.discord.Bot;
import indrocraft.indrocraftplugin.discord.botManager.BotEventListener;
import indrocraft.indrocraftplugin.commands.*;
import indrocraft.indrocraftplugin.utils.ConfigUtils;
import indrocraft.indrocraftplugin.utils.SQLConnector;
import indrocraft.indrocraftplugin.utils.SQLUtils;
import indrocraft.indrocraftplugin.events.JoinLeaveEvent;
import indrocraft.indrocraftplugin.events.RankEvents;
import indrocraft.indrocraftplugin.utils.RankUtils;
import net.dv8tion.jda.api.JDA;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin{

    public SQLConnector sqlconnector;
    public RankUtils rankUtils;
    public Bot bot;
    public SQLUtils sqlUtils;

    @Override
    public void onEnable() {
        // Plugin startup logic

        //initialize config files
        ConfigUtils config = new ConfigUtils(this, "config.yml");
        config.saveDefaultConfig();
        ConfigUtils ranks = new ConfigUtils(this, "rank.yml");
        ranks.saveDefaultConfig();
        ConfigUtils warps = new ConfigUtils(this, "warps.yml");
        warps.saveDefaultConfig();

        //initialize bot:
        //bot = new Bot("OTMxMjk5NjY0NTE3NTU4MzIy.YeCaZg.unpR0-QGsytO3eS7fKUBnQ_w-PU");

        //init utils
        //sqlManager initializes connection with the database:
        sqlconnector = new SQLConnector(config.getConfig().getString("database.database"),
                config.getConfig().getString("database.host"),
                config.getConfig().getString("database.port"),
                config.getConfig().getString("database.user"),
                config.getConfig().getString("database.password"),
                false,
                this);
        sqlUtils = new SQLUtils(sqlconnector);
        rankUtils = new RankUtils();

        // commands:
/*
        getServer().getPluginCommand("dev").setExecutor(new Dev());             used for testing
*/
        getServer().getPluginCommand("warn").setExecutor(new Warn());
        getServer().getPluginCommand("home").setExecutor(new Home());
        getServer().getPluginCommand("warp").setExecutor(new Warp());
        getServer().getPluginCommand("ranks").setExecutor(new RankCommand());

        // tab executors:
        getCommand("warn").setTabCompleter(new Warn());
        getCommand("home").setTabCompleter(new Home());
        getCommand("ranks").setTabCompleter(new RankCommand());

        //register events:
        getServer().getPluginManager().registerEvents(new JoinLeaveEvent(), this);
        getServer().getPluginManager().registerEvents(new RankEvents(), this);
        getServer().getPluginManager().registerEvents(new BotEventListener(), this);

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
        /*bot.getJda().shutdownNow();
        while (bot.getJda().getStatus() != JDA.Status.SHUTTING_DOWN) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        Bukkit.getLogger().info("Successfully disabled Indrocraft plugin!");
    }
}