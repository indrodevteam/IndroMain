package com.github.indrodevteam.indroMain.ranks;

import com.github.indrodevteam.indroMain.events.EventOnRankPromote;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RankUtils {

    public static HashMap<Rank, Boolean> getNextCriteria(Player player) {
        // init data
        Rank currentRank = UserRanks.getRank(player);
        HashMap<Rank, Boolean> criteriaStats = new HashMap<>();

        // checks next ranks
        ArrayList<String> playerNextRanks = currentRank.getNextRanks();
        for (String nextRank: playerNextRanks) {
            Rank checkedRank = RankStorage.readRank(nextRank);
            boolean check = true;
            if (checkedRank != null) {
                ArrayList<String> advancementsForRank = checkedRank.getAdvancementGate();
                if (advancementsForRank == null || advancementsForRank.isEmpty()) {
                    criteriaStats.put(checkedRank, true);
                    continue;
                }
                Iterator<Advancement> it = Bukkit.getServer().advancementIterator();
                // gets all 'registered' advancements on the server.
                while (it.hasNext()) {
                    // loops through these.
                    Advancement a = it.next();
                    if (advancementsForRank.contains(a.getKey().toString()) && !player.getAdvancementProgress(a).isDone()) {
                        check = false;
                    }
                }
            }
            criteriaStats.put(checkedRank, check);
        }
        return criteriaStats;
    }

    public static boolean getEntryCriteria(Player player, @NotNull Rank entryRank) {
        boolean check = true;
        ArrayList<String> advancementsForRank = entryRank.getAdvancementGate();
        if (advancementsForRank == null) {
            return true;
        }
        Iterator<Advancement> it = Bukkit.getServer().advancementIterator();
        // gets all 'registered' advancements on the server.
        while (it.hasNext()) {
            // loops through these.
            Advancement a = it.next();
            if (advancementsForRank.contains(a.getKey().toString()) && !player.getAdvancementProgress(a).isDone()) {
                check = false;
            }
        }
        return check;
    }

    public static void createRank(String rankName,
                                  String format,
                                  @Nullable ArrayList<String> nextRank,
                                  int maxHomes,
                                  @Nullable Integer discordID,
                                  @Nullable String... advancementGates) {
        ArrayList<String> advancementsRequired = null;
        if (advancementGates != null) {
            advancementsRequired = new ArrayList<>(List.of(advancementGates));
        }
        RankStorage.createRank(rankName, format, nextRank, maxHomes, discordID, advancementsRequired, null);
    }

    /**
     * Promoter for ranks (includes rank checker)
     * Does not include rank selector
     */
    public static void promoteUser(Player player, @NotNull Rank nextRank) {
        Rank currentRank = UserRanks.getRank(player);

        HashMap<Rank, Boolean> criteria = getNextCriteria(player);
        if (!criteria.containsKey(nextRank) || !criteria.get(nextRank)) {
            // promotion cannot be done
            return;
        }

        // Send event of promotion, for the api...
        EventOnRankPromote event = new EventOnRankPromote(player, currentRank, nextRank);
        UserRanks.setRank(player, nextRank);
        Bukkit.getPluginManager().callEvent(event);
    }

    public static String translate(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, ChatColor.of(color) + "");
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}