package io.github.indroDevTeam.indroMain.events;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.model.DaoPoint;
import io.github.indroDevTeam.indroMain.model.DaoProfile;
import io.github.indroDevTeam.indroMain.model.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class EventOnPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        Profile profile;
        try {
            if (DaoProfile.getInstance().find(player.getUniqueId()).isPresent()) {
                profile = DaoProfile.getInstance().find(player.getUniqueId()).get();
            } else {
                throw new SQLException("Null Value Exception");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        player.setDisplayName(profile.getRank().getChatTag() + player.getName());
        player.setPlayerListName(profile.getRank().getTabTag() + player.getName());
    }
}
