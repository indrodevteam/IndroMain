package indrocraft.indrocraftplugin;

import indrocraft.indrocraftplugin.commands.Dev;
import indrocraft.indrocraftplugin.dataManager.ConfigTools;
import indrocraft.indrocraftplugin.dataManager.MySQL;
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

    FileConfiguration config = getConfig();

    public File configA;

    public MySQL SQL;

    @Override
    public void onEnable() {
        // Plugin startup logic
        // commands:
        getServer().getPluginCommand("dev").setExecutor(new Dev(this));

        // create config files
        generateConfig("config.yml");
        generateConfig("rank.yml");

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
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void generateConfig(String configName) {
        File configA = new File(getDataFolder(), configName);

        if (!configA.exists()) {
            configA.getParentFile().mkdirs();
            saveResource(configName, false);
        }
        FileConfiguration config = new YamlConfiguration();

        try {
            config.load(configA);
        } catch (IOException | InvalidConfigurationException e ) {
            e.printStackTrace();
        }
    }
}