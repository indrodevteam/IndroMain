package io.github.indroDevTeam.indroMain.model;

import io.github.indroDevTeam.indroMain.IndroMain;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class Point {
    /* Variables */
    private UUID ownerId;
    private String name;
    private double x, y, z;
    private float pitch, yaw;
    private String worldName;
    
    /* Constructor */

    public Point(UUID ownerId, String name, double x, double y, double z, float pitch, float yaw, String worldName) {
        this.ownerId = ownerId;
        this.name = name;

        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.worldName = worldName;
    }

    public Point(UUID ownerId, String name, Location location) {
        this.ownerId = ownerId;
        this.name = name;

        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
        this.worldName = location.getWorld().getName();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Getters and Setters
    ///////////////////////////////////////////////////////////////////////////
    public Location getLocation() {
        return new Location(IndroMain.getInstance().getServer().getWorld(worldName), x, y, z, yaw, pitch);
    }

    /**
     * Gets the distance from the player
     * NB: worlds that aren't the same are noted to be 4x (configurable)
     * the distance away, as they are effectively in different worlds
     * @param player the initial location the player is
     * @return the distance from the player to the point, including different world equations
     */
    public double getDistance(Player player) {
        double val;
        if (player.getLocation().getWorld().equals(getLocation().getWorld())) {
            val = player.getLocation().distance(getLocation());
        } else {
            Vector playerVector = new Vector(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
            Vector destiVector = new Vector(getX(), getY(), getZ());

            val = playerVector.distance(destiVector) * IndroMain.getInstance().getConfig().getInt("warp.crossWorldMultiplier");
        }
        return val;
    }
}
