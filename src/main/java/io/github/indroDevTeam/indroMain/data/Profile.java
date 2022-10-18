package io.github.indroDevTeam.indroMain.data;

import java.util.List;
import java.util.UUID;

public class Profile {
    private final UUID playerId;
    private List<Point> points;
    private int warpCap, warpDelay, warpCooldown;

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

    public void addPoint(Point point) {
        points.add(point);
    }

    public void deletePoint(Point point) {
        points.remove(point);
    }


    /* points */
    public int getWarpCap() {
        return warpCap;
    }

    public int getWarpDelay() {
        return warpDelay;
    }

    public int getWarpCooldown() {
        return warpCooldown;
    }
}
