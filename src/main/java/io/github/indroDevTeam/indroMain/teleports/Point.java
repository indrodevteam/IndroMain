package io.github.indroDevTeam.indroMain.teleports;

import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Point {
    // identification for homes
    private String homeName;
    private String owner;
    private String pointType;

    // location for home
    private float x;
    private float y;
    private float z;
    private float pitch;
    private float yaw;
    private String worldName;

    /**
     * Creates a point using the Location datatype
     * @param pointType the type of point used
     * @param homeName the name of the home
     * @param owner an identifying key that the program can identify with
     * @param homePos the location of the point
     * @param invitees if the point type is shared, the uuids of the users permitted
     */
    public Point(String pointType,
                 String homeName,
                 String owner,
                 Location homePos,
                 @Nullable UUID... invitees) {
        this.pointType = pointType;
        this.homeName = homeName;
        this.owner = owner;

        this.x = homePos.getBlockX();
        this.y = homePos.getBlockY();
        this.z = homePos.getBlockZ();
        this.pitch = homePos.getPitch();
        this.yaw = homePos.getYaw();
        this.worldName = homePos.getWorld().getName();
    }

    public Point(String pointType,
                 String homeName,
                 String owner,
                 float x, float y, float z, float pitch, float yaw, String worldName,
                 @Nullable UUID... invitees) {
        this.pointType = pointType;
        this.homeName = homeName;
        this.owner = owner;

        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.worldName = worldName;
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

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public String getPointType() {
        return pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }
}
