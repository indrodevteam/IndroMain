package io.github.indrodevteam.indroMain.model;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import io.github.indrodevteam.indroMain.IndroMain;

import java.io.Serializable;
import java.util.UUID;

public class Point implements Serializable {
    /* Variables */
    protected String id;
    protected UUID playerId;
    protected String name;
    protected double x, y, z;
    protected float pitch, yaw;
    protected String worldName;
    
    /* Constructor */

    public Point(String name, double x, double y, double z, float pitch, float yaw, String worldName) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.worldName = worldName;
    }
    
    public Point(String name, Location location) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }
}
