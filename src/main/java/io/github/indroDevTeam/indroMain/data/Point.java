package io.github.indroDevTeam.indroMain.data;

import org.bukkit.Location;

import io.github.indroDevTeam.indroMain.IndroMain;

public class Point {
    /* Variables */
    private String name;
    private double x, y, z;
    private float pitch, yaw;
    private String worldName;
    
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

    /* Getters and Setters */
    public Location getLocation() {
        return new Location(IndroMain.getInstance().getServer().getWorld(worldName), x, y, z);
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
