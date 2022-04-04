package io.github.indroDevTeam.indroMain.events;

import io.github.indroDevTeam.indroMain.dataUtils.LanguageTags;
import io.github.indroDevTeam.indroMain.ranks.Rank;
import io.github.indroDevTeam.indroMain.ranks.RankUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import java.util.HashMap;

public class EventOnAdvancement implements Listener {
    @EventHandler
    public void onAdvancementGet(PlayerAdvancementDoneEvent event) {
        HashMap<Rank, Boolean> rankBooleanHashMap = RankUtils.getNextCriteria(event.getPlayer());
        if (rankBooleanHashMap.isEmpty()) {
            return;
        }

        for (Rank rank1: rankBooleanHashMap.keySet()) {
            if (rankBooleanHashMap.get(rank1)) {
                event.getPlayer().sendMessage(LanguageTags.RANK_PROMOTION.get());
                return;
            }
        }
    }
}
