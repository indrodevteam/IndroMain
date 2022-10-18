package io.github.indroDevTeam.indroMain.data;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class Profile implements Serializable {
    private UUID playerId;
    private List<Point> points;
    private int warpCap, warpDelay, warpCooldown, maxDistance;
    private boolean crossWorldPermitted;

    public Profile() {}

    ///////////////////////////////////////////////////////////////////////////
    // Class-based Data
    ///////////////////////////////////////////////////////////////////////////

    @Nullable
    public Point getPoint(String name) {
        for (Point p: points) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Getters and Setters
    ///////////////////////////////////////////////////////////////////////////
    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public int getWarpCap() {
        return warpCap;
    }

    public void setWarpCap(int warpCap) {
        this.warpCap = warpCap;
    }

    public int getWarpDelay() {
        return warpDelay;
    }

    public void setWarpDelay(int warpDelay) {
        this.warpDelay = warpDelay;
    }

    public int getWarpCooldown() {
        return warpCooldown;
    }

    public void setWarpCooldown(int warpCooldown) {
        this.warpCooldown = warpCooldown;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public boolean isCrossWorldPermitted() {
        return crossWorldPermitted;
    }

    public void setCrossWorldPermitted(boolean crossWorldPermitted) {
        this.crossWorldPermitted = crossWorldPermitted;
    }
}
