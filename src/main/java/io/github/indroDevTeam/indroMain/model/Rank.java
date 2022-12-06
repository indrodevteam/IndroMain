package io.github.indroDevTeam.indroMain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rank {
    /* Variables */
    private String name;
    private String chatTag;
    private String tabTag;

    // warp data
    private int warpCap;
    private int warpDelay;
    private int warpCooldown;
    private int maxDistance;
    private boolean crossWorldPermitted;

    /* Constructors */
    public Rank(String name, String chatTag, String tabTag,
                int warpCap, int warpDelay, int warpCooldown, int maxDistance, boolean crossWorldPermitted) {
        this.name = name;
        this.chatTag = chatTag;
        this.tabTag = tabTag;
        this.warpCap = warpCap;
        this.warpDelay = warpDelay;
        this.warpCooldown = warpCooldown;
        this.maxDistance = maxDistance;
        this.crossWorldPermitted = crossWorldPermitted;
    }
}
