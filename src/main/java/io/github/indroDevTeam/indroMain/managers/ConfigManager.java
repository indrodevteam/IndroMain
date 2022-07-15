package io.github.indroDevTeam.indroMain.managers;

import io.github.indroDevTeam.indroMain.IndroMain;
import lombok.Getter;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

@Getter
public class ConfigManager {

    // program protocols
    private final File main;
    private final FileConfiguration config;
    private final IndroMain plugin;

    // settigns
    private int privateWarpRange;

    // messages
    private String messageWarpedDeniedDistance;

    // file logic
    private boolean useThreads;
    private boolean savePeriodically;
    private int saveInterval;

    public ConfigManager() {
        plugin = IndroMain.getInstance();
        config = plugin.getConfig();
        main = new File(plugin.getDataFolder() + File.separator + "config.yml");
        load();
    }

    @SuppressWarnings({"CallToPrintStackTrace", "UseSpecificCatch"})
    public void load() {
        boolean exists = (main).exists();

        if (exists) {
            try {
                getConfig().options().copyDefaults(true);
                getConfig().load(main);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getConfig().options().copyDefaults(true);
        }

        // get file settings

        useThreads = getConfig().getBoolean("performance.useThreads");
        messageWarpedDeniedDistance = getConfig().getString("warp.privateWarpRadius");

        save();
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void save() {
        try {
            getConfig().save(main);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Magic Value
     * @param path
     * @return
     */
    public Object getValue(String path) {
        return getConfig().get(path);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Getters and Setters
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Gets the interval to save the data
     *
     * @return the interval in seconds
     */
    public int getSaveInterval() {
        if (saveInterval < 1) {
            saveInterval = 5;
        }

        return saveInterval * 60;
    }
}
