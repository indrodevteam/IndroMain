package io.github.indroDevTeam.indroMain.managers;

import io.github.indroDevTeam.indroMain.IndroMain;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class SettingsManager {
    private final File main;
    private final FileConfiguration config;
    private final IndroMain plugin;
    private boolean useThreads;
    private boolean savePeriodically;
    private int saveInterval;

    public SettingsManager() {
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

        //get file settings

        useThreads = getConfig().getBoolean("performance.useThreads");

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

    ///////////////////////////////////////////////////////////////////////////
    // Getters and Setters
    ///////////////////////////////////////////////////////////////////////////


    public boolean getUseThreads() {
        return useThreads;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public boolean isSavePeriodically() {
        return savePeriodically;
    }

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
