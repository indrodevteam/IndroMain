package io.github.indroDevTeam.indroMain.events;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.model.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventOnPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        Profile profile = Profile.getNewProfile(player, "default");
        if (IndroMain.getDataManager().getProfileDao().get(player.getUniqueId()).isPresent()) {
            profile = IndroMain.getDataManager().getProfileDao().get(player.getUniqueId()).get();
        } else {
            IndroMain.getDataManager().getProfileDao().insert(profile);
        }

        player.setDisplayName(profile.getRank().getChatTag() + player.getName());
        player.setPlayerListName(profile.getRank().getTabTag() + player.getName());
    }
}
