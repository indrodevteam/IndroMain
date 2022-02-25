package io.github.indroDevTeam.indroMain.teleports;

import org.bukkit.Location;
import org.bukkit.World;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Point {
    // identification for homes
    private String homeName;
    private String owner;
    private UUID homeID;
    private PointType pointType;

    // location for home
    private float x;
    private float y;
    private float z;
    private float pitch;
    private float yaw;
    private World world;

    public Point(PointType pointType, String homeName, String owner, Location homePos) {
        this.pointType = pointType;
        this.homeName = homeName;
        this.owner = owner;
        this.homeID = UUID.nameUUIDFromBytes(homeName.getBytes(StandardCharsets.UTF_8));

        this.x = homePos.getBlockX();
        this.y = homePos.getBlockY();
        this.z = homePos.getBlockZ();
        this.pitch = homePos.getPitch();
        this.yaw = homePos.getYaw();
        this.world = homePos.getWorld();
    }

    public Point(PointType pointType, String homeName, String owner, float x, float y, float z, float pitch, float yaw, World world) {
        this.pointType = pointType;
        this.homeName = homeName;
        this.owner = owner;
        this.homeID = UUID.nameUUIDFromBytes(homeName.getBytes(StandardCharsets.UTF_8));

        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.world = world;
    }

    // getters and setters

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public UUID getHomeID() {
        return homeID;
    }

    public void setHomeID(UUID homeID) {
        this.homeID = homeID;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
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

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public PointType getPointType() {
        return pointType;
    }

    public void setPointType(PointType pointType) {
        this.pointType = pointType;
    }
}
