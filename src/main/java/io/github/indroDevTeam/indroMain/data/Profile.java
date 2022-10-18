package io.github.indroDevTeam.indroMain.data;

import java.util.List;
import java.util.UUID;

public class Profile {
    private final UUID playerId;
    private List<Point> points;
    private int warpCap, warpDelay, warpCooldown, maxDistance;

    public Profile(UUID playerId, List<Point> points, int warpCap, int warpDelay, int warpCooldown) {
        this.playerId = playerId;
        this.points = points;
        this.warpCap = warpCap;
        this.warpDelay = warpDelay;
        this.warpCooldown = warpCooldown;
    }

    public UUID getPlayerId() {
        return playerId;
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

    
}
