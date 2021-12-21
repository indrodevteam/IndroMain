package indrocraft.indrocraftplugin.utils;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.dataManager.ConfigTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RankUtils {

    private Main main = Main.getPlugin(Main.class);

    public ConfigTools config = new ConfigTools(main, "rank.yml");

    public void levelUp(Player player, SQLUtils data, String newRank) {
        setRank(player, data, newRank);
        LoadRank(player, data);
    }

    public int getLevel(Player player, SQLUtils data) {
        String rank = data.getString("rank", "UUID",player.getUniqueId().toString(), "players");
        String code = config.getConfig().getString("ranks." + rank + ".details.level");
        return Integer.parseInt(code);
    }

    public void setRank(Player player, SQLUtils sqlUtils, String newRank) {
        sqlUtils.setData(newRank, "UUID", player.getUniqueId().toString(), "`rank`", "players");
    }

    public void setNameColour(Player player, SQLUtils data, String newColour) {
        data.setData(newColour, "UUID", player.getUniqueId().toString(), "nameColour", "players");
    }

    public void LoadRank(Player player, SQLUtils data) {
        String uuid = player.getUniqueId().toString();
        String displayName = data.getString("rank", "UUID", uuid, "players");

        List<String> ranks = new ArrayList<>();
        ranks.addAll(config.getConfig().getConfigurationSection("ranks").getKeys(false));
        if (!ranks.contains(displayName)) {
            data.setData(ranks.get(0), "UUID", uuid, "rank", "players");
            displayName = data.getString("rank", "UUID", uuid, "players");
        }

        try {
            ChatColor pc = readColour(config.getConfig().getString("ranks." + displayName + ".colours.primary"));
            ChatColor sc = readColour(config.getConfig().getString("ranks." + displayName + ".colours.secondary"));
            ChatColor nc = readColour(main.sqlUtils.getString("nameColour", "UUID", uuid, "players"));
            player.setDisplayName(sc + "[" + pc + displayName + sc + "] " + nc + player.getName() + ChatColor.WHITE + "");
            player.setPlayerListName(sc + "[" + pc + displayName + sc + "] " + nc + player.getName() + ChatColor.WHITE + "");
        } catch (NullPointerException e) {
            Bukkit.getLogger().severe("Could not apply default rank in rank.yml");
        }
    }

    public ChatColor readColour(String color) {
        if (color.equalsIgnoreCase("white")) {
            return ChatColor.WHITE;
        } else if (color.equalsIgnoreCase("gray")) {
            return ChatColor.GRAY;
        } else if (color.equalsIgnoreCase("dark_gray")) {
            return ChatColor.DARK_GRAY;
        } else if (color.equalsIgnoreCase("black")) {
            return ChatColor.BLACK;
        } else if (color.equalsIgnoreCase("dark_red")) {
            return ChatColor.DARK_RED;
        } else if (color.equalsIgnoreCase("red")) {
            return ChatColor.RED;
        } else if (color.equalsIgnoreCase("gold")) {
            return ChatColor.GOLD;
        } else if (color.equalsIgnoreCase("yellow")) {
            return ChatColor.YELLOW;
        } else if (color.equalsIgnoreCase("dark_green")) {
            return ChatColor.DARK_GREEN;
        } else if (color.equalsIgnoreCase("green")) {
            return ChatColor.GREEN;
        } else if (color.equalsIgnoreCase("aqua")) {
            return ChatColor.AQUA;
        } else if (color.equalsIgnoreCase("dark_aqua")) {
            return ChatColor.DARK_AQUA;
        } else if (color.equalsIgnoreCase("dark_blue")) {
            return ChatColor.DARK_BLUE;
        } else if (color.equalsIgnoreCase("blue")) {
            return ChatColor.BLUE;
        } else if (color.equalsIgnoreCase("light_purple")) {
            return ChatColor.LIGHT_PURPLE;
        } else if (color.equalsIgnoreCase("dark_purple")) {
            return ChatColor.DARK_PURPLE;
        } else {
            Bukkit.getLogger().warning("'" + color + "' is an invalid colour! try reloading the plugin.");
            Bukkit.getLogger().warning("Defaulting to white!");
            return ChatColor.WHITE;
        }
    }
}
