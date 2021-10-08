package indrocraft.indrocraftplugin;

import indrocraft.indrocraftplugin.commands.Dev;
import indrocraft.indrocraftplugin.dataManager.ConfigTools;
import indrocraft.indrocraftplugin.dataManager.MySQL;
import indrocraft.indrocraftplugin.utils.SQLUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;


public final class Main extends JavaPlugin implements Listener {

    public MySQL SQL;
    public SQLUtils sqlUtils;
    public ConfigTools configTools;

    @Override
    public void onEnable() {
        // Plugin startup logic
        //init utils
        sqlUtils = new SQLUtils(this);
        configTools = new ConfigTools(this);
        SQL = new MySQL(this);

        // commands:
        getServer().getPluginCommand("dev").setExecutor(new Dev(this));

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

        // testing:
        sqlUtils.createTable("testing", "NAME");
        sqlUtils.createColumn("test", "DOUBLE", "testing");
        sqlUtils.createRow("NAME", "player", "testing");

        sqlUtils.setDataType("test", "VARCHAR(100)", "testing");
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SQL.disconnect();
    }
}