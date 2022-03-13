package io.github.indroDevTeam.indroMain.dataUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;

public class LanguageLoader {
    private final FileConfiguration resourceBundle;

    public LanguageLoader(){
        YamlUtils yamlUtils = new YamlUtils("en_us");
        if (yamlUtils.isFileExist()) {
            yamlUtils.loadFromFile();
        } else {
            try {
                Bukkit.getServer().getLogger().severe("Language locale does not exist, using stored file!");
                yamlUtils.loadFromResource();
                yamlUtils.createFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            yamlUtils.saveFile(yamlUtils.getConfig());
        }
        resourceBundle = yamlUtils.getConfig();
    }

    public String get(String path){
        String message = resourceBundle.getString(path);
        if (message != null) {
            return ChatColor.translateAlternateColorCodes('&', message);
        } else {
            return null;
        }
    }
}