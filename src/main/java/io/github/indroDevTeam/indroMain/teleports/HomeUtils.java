package io.github.indroDevTeam.indroMain.teleports;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HomeUtils {

    public void createHome(String homeName, Player player, Location location) {
        new Point(PointType.PRIVATE_HOME, homeName, player.getUniqueId().toString(), location);
    }
}
