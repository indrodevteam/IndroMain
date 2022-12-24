package io.github.indroDevTeam.indroMain.model;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.advancement.Advancement;

import java.util.List;

@Getter
@Setter
public class Rank {
    /* Variables */
    private String name;
    private String chatTag;
    private String tabTag;

    // rank promotions
    private List<String> nextRanks;
    private List<Advancement> advanceRequired;

    // warp data
    private int warpCap;
    private int warpDelay;
    private int warpCooldown;
    private int maxDistance;
    private boolean crossWorldPermitted;

    /* Constructors */
    public Rank(String name, String chatTag, String tabTag,
                List<String> nextRanks, List<Advancement> advanceRequired,
                int warpCap, int warpDelay, int warpCooldown, int maxDistance, boolean crossWorldPermitted) {
        this.name = name;
        this.chatTag = chatTag;
        this.tabTag = tabTag;
        this.nextRanks = nextRanks;
        this.advanceRequired = advanceRequired;
        this.warpCap = warpCap;
        this.warpDelay = warpDelay;
        this.warpCooldown = warpCooldown;
        this.maxDistance = maxDistance;
        this.crossWorldPermitted = crossWorldPermitted;
    }
}
