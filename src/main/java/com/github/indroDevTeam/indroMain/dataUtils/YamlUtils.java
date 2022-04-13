package com.github.indroDevTeam.indroMain.dataUtils;

import com.github.indroDevTeam.indroMain.IndroMain;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class YamlUtils {
    private final File file;
    private YamlConfiguration config;

    public YamlUtils(String filename) {
        if (!filename.contains(".yml")) {
            filename += ".yml";
        }
        this.file = new File(IndroMain.getInstance().getDataFolder().getAbsolutePath() + File.separator + filename);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public YamlConfiguration getConfig() {
       return config;
    }

    public File getFile() {
        return file;
    }

    // create a custom file
    public void createFile() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().severe("File could not be created!");
                e.printStackTrace();
                return;
            }
            Bukkit.getLogger().info("File successfully created!");
        }
    }


    public void loadFromResource() throws IOException {
        InputStream is = IndroMain.getInstance().getResource(file.getName() + ".yml");
        OutputStream os = new FileOutputStream(file);
        byte[] buffer = new byte[4096];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        os.close();
        is.close();
    }

    public void loadFromFile() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void saveFile(FileConfiguration config) {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isFileExist() {
        return file.exists();
    }
}