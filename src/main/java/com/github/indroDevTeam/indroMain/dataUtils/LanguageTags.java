package com.github.indroDevTeam.indroMain.dataUtils;

import org.bukkit.ChatColor;

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
    RANK_PROMOTION("You can now promote to a new rank!"),

    AUTOSAVING("Auto_saving...");

    final String defaultValue;

    LanguageTags(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String get() {
        String message;
        if (defaultValue.equals(PLUGIN_TITLE.defaultValue)) {
            message = defaultValue;
            return ChatColor.translateAlternateColorCodes('&', message);
        }
        message = PLUGIN_TITLE.defaultValue + defaultValue;
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}