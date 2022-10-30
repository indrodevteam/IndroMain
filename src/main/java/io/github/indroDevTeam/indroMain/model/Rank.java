package io.github.indroDevTeam.indroMain.model;

public class Rank {
    protected String id;

    protected String name;
    protected String chatTag;
    protected String tabTag;

    // warp data
    protected int warpCap;
    protected int warpDelay;
    protected int warpCooldown;
    protected int maxDistance;
    protected boolean crossWorldPermitted;

    public Rank(String id) {
        this.id = id;
    }

    public Rank(String id, String name, String chatTag, String tabTag, int warpCap, int warpDelay, int warpCooldown, int maxDistance, boolean crossWorldPermitted) {
        this(name, chatTag, tabTag, warpCap, warpDelay, warpCooldown, maxDistance, crossWorldPermitted);
        this.id = id;
    }

    public Rank(String name, String chatTag, String tabTag, int warpCap, int warpDelay, int warpCooldown, int maxDistance, boolean crossWorldPermitted) {
        this.name = name;
        this.chatTag = chatTag;
        this.tabTag = tabTag;
        this.warpCap = warpCap;
        this.warpDelay = warpDelay;
        this.warpCooldown = warpCooldown;
        this.maxDistance = maxDistance;
        this.crossWorldPermitted = crossWorldPermitted;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Getters and Setters
    ///////////////////////////////////////////////////////////////////////////

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChatTag() {
        return chatTag;
    }

    public void setChatTag(String chatTag) {
        this.chatTag = chatTag;
    }

    public String getTabTag() {
        return tabTag;
    }

    public void setTabTag(String tabTag) {
        this.tabTag = tabTag;
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
