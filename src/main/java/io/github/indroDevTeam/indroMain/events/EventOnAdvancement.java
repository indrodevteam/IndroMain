package io.github.indroDevTeam.indroMain.events;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.model.Profile;
import io.github.indroDevTeam.indroMain.utils.ChatUtils;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import java.util.Map;

public class EventOnAdvancement implements Listener {
    @EventHandler
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent e) {
        Player player = e.getPlayer();
        Profile profile;
        if (IndroMain.getDataManager().getProfile(player.getUniqueId()).isEmpty()) {
            IndroMain.getDataManager().createProfile(Profile.getNewProfile(player, "default"));
        }
        profile = IndroMain.getDataManager().getProfile(player.getUniqueId()).get();


        Map<Advancement, Boolean> map = profile.getRank().isPlayerPromotable(player);
        for (Boolean b: map.values()) if (!b) return;
        if (map.containsKey(e.getAdvancement())) {
            ChatUtils.sendSuccess(player, "You have fulfilled all of the criteria needed to promote!");
        }
    }
}
