package io.github.indroDevTeam.indroMain.events;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.model.Profile;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

public class EventOnPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        Optional<Profile> profile = IndroMain.getDataManager().getProfile(player.getUniqueId());
        if (profile.isEmpty()) {
            profile = Optional.of(Profile.getNewProfile(player, "default"));
            IndroMain.getDataManager().createProfile(profile.get());
        }

        player.setDisplayName(ColorTranslator.translateColorCodes(profile.get().getRank().getChatTag() + player.getName()));
        player.setPlayerListName(ColorTranslator.translateColorCodes(profile.get().getRank().getTabTag() + player.getName()));
    }
}
