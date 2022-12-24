package io.github.indroDevTeam.indroMain.utils;

import me.kodysimpson.simpapi.colors.ColorTranslator;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatUtils {
    public static void sendError(CommandSender s, String message) {
        s.sendMessage(ChatColor.RED + message);
    }

    public static void sendFailure(CommandSender s, String message) {
        s.sendMessage(ChatColor.LIGHT_PURPLE + message);
    }

    public static void sendSuccess(CommandSender s, String message) {
        s.sendMessage(ChatColor.BLUE + message);
    }

    public static void sendWarning(CommandSender s, String message) {
        s.sendMessage(ChatColor.GOLD + message);
    }

    public static void notPlayerError(CommandSender s) {
        s.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command!");
    }

    public static void permissionError(CommandSender s) {
        sendError(s, "You don't have permission to do that!");
    }

    public static void syntaxError(CommandSender s) {
        sendError(s, "ERROR: Syntax Error!");
    }

    public static String format(String message) {
        return ColorTranslator.translateColorCodes(message);
    }
}
