package indrocraft.indrocraftplugin.utils;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.events.ServerRankEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class RankUtils {

    private final Main main = Main.getPlugin(Main.class);

    public ConfigUtils config = new ConfigUtils(main, "rank.yml");

    public int getLevel(Player player, SQLUtils data) {
        String rank = data.getString("rank", "UUID",player.getUniqueId().toString(), "players");
        String code = config.getConfig().getString("ranks." + rank + ".details.level");
        return Integer.parseInt(code);
    }

    public void setRank(Player player, SQLUtils sqlUtils, String newRank) {
        sqlUtils.setData(newRank, "UUID", player.getUniqueId().toString(), "rank", "players");

        String advancement = config.getConfig().getString("ranks." + newRank + ".details.nextAdvancement");

        ServerRankEvent rankEvent = new ServerRankEvent(player, advancement, newRank);
        Bukkit.getPluginManager().callEvent(rankEvent);
    }

    public void setNameColour(Player player, SQLUtils data, String newColour) {
        data.setData(newColour, "UUID", player.getUniqueId().toString(), "nameColour", "players");
    }

    public void LoadRank(Player player, SQLUtils data) {
        String uuid = player.getUniqueId().toString();
        String displayName = data.getString("rank", "UUID", uuid, "players");

        List<String> ranks = new ArrayList<>(Objects.requireNonNull(config.getConfig().
                getConfigurationSection("ranks")).getKeys(false));
        if (!ranks.contains(displayName)) {
            data.setData(ranks.get(0), "UUID", uuid, "rank", "players");
            displayName = data.getString("rank", "UUID", uuid, "players");
        }

        try {
            ChatColor pc = readColour(config.getConfig().getString("ranks." + displayName + ".colours.primary"));
            ChatColor sc = readColour(config.getConfig().getString("ranks." + displayName + ".colours.secondary"));
            ChatColor nc = readColour(data.getString("nameColour", "UUID", uuid, "players"));
            player.setCustomName(sc + "[" + pc + displayName + sc + "] " + nc + player.getName() + ChatColor.WHITE + "");
            player.setCustomNameVisible(true);
            player.setDisplayName(sc + "[" + pc + displayName + sc + "] " + nc + player.getName() + ChatColor.WHITE + "");
            player.setPlayerListName(sc + "[" + pc + displayName + sc + "] " + nc + player.getName() + ChatColor.WHITE + "");
        } catch (NullPointerException e) {
            Bukkit.getLogger().severe("Could not apply default rank in rank.yml");
        }
    }

    public boolean hasAdvancement(Player player, String name) {
        // name should be something like minecraft:husbandry/break_diamond_hoe
        Advancement a = getAdvancement(name);
        if(a == null){
            // advancement does not exists.
            return false;
        }
        AdvancementProgress progress = player.getAdvancementProgress(a);
        // getting the progress of this advancement.
        return progress.isDone();
        //returns true or false.
    }

    public Advancement getAdvancement(String name) {
        Iterator<Advancement> it = Bukkit.getServer().advancementIterator();
        // gets all 'registered' advancements on the server.
        while (it.hasNext()) {
            // loops through these.
            Advancement a = it.next();
            if (a.getKey().toString().equalsIgnoreCase(name)) {
                //checks if one of these has the same name as the one you asked for. If so, this is the one it will return.
                return a;
            }
        }
        return null;
    }

    public ChatColor readColour(String color) {
        switch (color.toLowerCase()) {
            case "dark_red": return ChatColor.DARK_RED;
            case "red": return ChatColor.RED;
            case "gold": return ChatColor.GOLD;
            case "yellow": return ChatColor.YELLOW;
            case "dark_green": return ChatColor.DARK_GREEN;
            case "green": return ChatColor.GREEN;
            case "aqua": return ChatColor.AQUA;
            case "dark_aqua": return ChatColor.DARK_AQUA;
            case "dark_blue": return ChatColor.DARK_BLUE;
            case "blue": return ChatColor.BLUE;
            case "light_purple": return ChatColor.LIGHT_PURPLE;
            case "dark_purple": return ChatColor.DARK_PURPLE;
            case "white": return ChatColor.WHITE;
            case "gray": return ChatColor.GRAY;
            case "dark_gray": return ChatColor.DARK_GRAY;
            case "black": return ChatColor.BLACK;
            default:
                Bukkit.getLogger().warning("'" + color + "' is an invalid colour! try reloading the plugin.");
                Bukkit.getLogger().warning("Defaulting to white!");
                return ChatColor.WHITE;
        }
    }
}
