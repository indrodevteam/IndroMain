package indrocraft.indrocraftplugin.dataManager;

import indrocraft.indrocraftplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigTools {

    private static Main main;
    public ConfigTools(Main main) {this.main = main;}

    public static FileConfiguration getFileConfig(String fileName) {
        File configFile = new File(Bukkit.getServer().getWorldContainer().getAbsolutePath() + "/plugins/IndrocraftPlugin/"+fileName); // First we
        // will load
        // the file.
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile); // Now we will load the file into a
        // FileConfiguration.
        return config;
    }

    public static void generateConfig(String configName) {
        File configA = new File(main.getDataFolder(), configName);

        if (!configA.exists()) {
            configA.getParentFile().mkdirs();
            main.saveResource(configName, false);
        }
        FileConfiguration config = new YamlConfiguration();

        try {
            config.load(configA);
        } catch (IOException | InvalidConfigurationException e ) {
            e.printStackTrace();
        }
    }
}
