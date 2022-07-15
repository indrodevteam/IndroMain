package io.github.indroDevTeam.indroMain.points;

public class Point {
    private String name;
    private String ownerUUID;
    private int locX, locY, locZ;
    private float locPitch, locYaw;
    private String locWorldName;
    private


    public enum PointType {
        PUBLIC_WARP,
        PRIVATE_WARP;
    }
}

