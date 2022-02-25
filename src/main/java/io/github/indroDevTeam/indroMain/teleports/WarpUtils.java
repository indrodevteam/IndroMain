package io.github.indroDevTeam.indroMain.teleports;

import io.github.indroDevTeam.indroMain.IndroMain;
import org.bukkit.Location;

public class WarpUtils {
    public void createWarp(String warpName, Location location) {
        String serverName = IndroMain.getInstance().getName();
        new Point(warpName, serverName, location);
    }
}
