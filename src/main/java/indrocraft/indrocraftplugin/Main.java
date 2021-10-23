package indrocraft.indrocraftplugin;

import indrocraft.indrocraftplugin.commands.*;
import indrocraft.indrocraftplugin.dataManager.ConfigTools;
import indrocraft.indrocraftplugin.dataManager.MySQL;
import indrocraft.indrocraftplugin.events.JoinLeaveEvent;
import indrocraft.indrocraftplugin.events.RankEvents;
import indrocraft.indrocraftplugin.utils.RankUtils;
import indrocraft.indrocraftplugin.utils.SQLUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;


public final class Main extends JavaPlugin implements Listener {

    public MySQL SQL;
    public SQLUtils sqlUtils;
    public ConfigTools configTools;
    public RankUtils rankUtils;

    @Override
    public void onEnable() {
        // Plugin startup logic
        //init utils
        sqlUtils = new SQLUtils(this);
        configTools = new ConfigTools(this);
        SQL = new MySQL(this);
        rankUtils = new RankUtils();

        // commands:
        getServer().getPluginCommand("dev").setExecutor(new Dev(this));
        getServer().getPluginCommand("setRank").setExecutor(new SetRank(this));
        getServer().getPluginCommand("warn").setExecutor(new Warn(this));
        getServer().getPluginCommand("home").setExecutor(new Home(this));
        getServer().getPluginCommand("inspector").setExecutor(new Inspector(this));
        getServer().getPluginCommand("warp").setExecutor(new Warp(this));

        // tab executors:
        getCommand("warn").setTabCompleter(new Warn(this));
        getCommand("home").setTabCompleter(new Home(this));

        // create config files
        configTools.generateConfig("config.yml");
        configTools.generateConfig("rank.yml");

        // connects to the database:
        this.SQL = new MySQL(this);

        try {
            SQL.connect();
        } catch (ClassNotFoundException | SQLException e) {
            Bukkit.getLogger().severe("Database not connected!");
        }

        if (SQL.isConnected()) {
            Bukkit.getLogger().info(ChatColor.BLUE + "Database is connected!");
        }

        //register events:
        getServer().getPluginManager().registerEvents(new JoinLeaveEvent(this), this);
        getServer().getPluginManager().registerEvents(new RankEvents(this), this);

        try {
            //create tables:
            sqlUtils.createTable("players", "UUID");
            sqlUtils.createColumn("ign", "VARCHAR(100)", "players");
            sqlUtils.createColumn("warns", "INT(100)", "players");

            // testing:
            sqlUtils.createTable("testing", "NAME");
            sqlUtils.createColumn("test", "DOUBLE", "testing");
            sqlUtils.createRow("NAME", "player", "testing");

            sqlUtils.setDataType("test", "VARCHAR(100)", "testing");
        } catch (Exception e) {
            Bukkit.getLogger().warning("testing canceled please connect database");
        }
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SQL.disconnect();
    }
}