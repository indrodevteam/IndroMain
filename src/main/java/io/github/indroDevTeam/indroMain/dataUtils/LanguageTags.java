package io.github.indroDevTeam.indroMain.dataUtils;

import io.github.indroDevTeam.indroMain.IndroMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.HashMap;

public enum LanguageTags {
    PLUGIN_TITLE("&d[&5IndroMain&d]&b "),
    JUMP_SUCCESS("Jumping!"),
    JUMP_FAILED("Jump spot critical! Cancelling warp!"),
    JUMP_LOADING("Jump spot loading..."),

    SET_HOME_SUCCESS("Successfully saved a home!"),
    SET_WARP_SUCCESS("Successfully saved a warp!"),
    DEL_HOME_SUCCESS("Successfully cleared this home!"),
    DEL_WARP_SUCCESS("Successfully cleared this warp!"),

    ERROR_POINT_EXIST("Error! This point does not exist!"),
    ERROR_SYNTAX("Error! Uninterpretable!"),
    ERROR_PERMISSION("Error! ID level too low!"),
    ERROR_FILE_ACCESS("Error! File access could not be established!"),
    ERROR_PLAYER_ONLY("Error! This command is player only!"),
    ERROR_RANK_EXIST("Error: Rank selected does not exist!"),

    RELOAD_SUCCESS("Reload Successful!"),
    RANK_UP("You've fulfilled all criteria! Upgrading to rank %rank_name%!"),

    AUTOSAVING("Auto_saving...");

    final String defaultValue;
    //private HashMap<LanguageTags, String> finalValue;

    LanguageTags(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String get() {
        String message = PLUGIN_TITLE.defaultValue + defaultValue;
        return ChatColor.translateAlternateColorCodes('&', message);
    }
/*
    public void init() {
        YamlUtils yamlUtils = new YamlUtils("lang.yml");
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
        FileConfiguration resourceBundle = yamlUtils.getConfig();
        ConfigurationSection section = resourceBundle.getConfigurationSection("");
        if (section == null) {
            Bukkit.getServer().getLogger().warning("Language Files incorrectly set up! Stopping plugin load!");
            IndroMain.getInstance().getPluginLoader().disablePlugin(IndroMain.getInstance());
            return;
        }

        // set up keys
        for (int i = 0; i < section.getKeys(false).size(); i++) {
            if (section.getKeys(false).contains(LanguageTags.values()[i].toString())) {
                finalValue.put(LanguageTags.values()[i], section.getString(LanguageTags.values()[i].toString()));
            } else {
                Bukkit.getServer().getLogger().warning("Language Files incorrectly set up! Stopping plugin load!");
                IndroMain.getInstance().getPluginLoader().disablePlugin(IndroMain.getInstance());
                break;
            }
        }

    }

    public String get(String path){
        String message = this.langTags.get(LanguageTags.valueOf(path));
        if (message != null) {
            return ChatColor.translateAlternateColorCodes('&', message);
        }
        // this realistically shouldn't fire
        return path;
    }
 */
}