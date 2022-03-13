package io.github.indroDevTeam.indroMain.ranks;

import java.util.ArrayList;
import java.util.List;

public class Rank {
    private String rankName;
    private String format;
    private String nextAdvancement;
    private int maxHomes;
    private String nextRank;
    private Integer discordID;

    public Rank(String rankName, String format, String nextAdvancement, int maxHomes, String nextRank, Integer discordID) {
        this.rankName = rankName;
        this.format = format;
        this.nextAdvancement = nextAdvancement;
        this.maxHomes = maxHomes;
        this.nextRank = nextRank;
        this.discordID = discordID;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getNextAdvancement() {
        return nextAdvancement;
    }

    public void setNextAdvancement(String nextAdvancement) {
        this.nextAdvancement = nextAdvancement;
    }

    public int getMaxHomes() {
        return maxHomes;
    }

    public void setMaxHomes(int maxHomes) {
        this.maxHomes = maxHomes;
    }

    public String getNextRank() {
        return nextRank;
    }

    public void setNextRank(String nextRank) {
        this.nextRank = nextRank;
    }

    public Integer getDiscordID() {
        return discordID;
    }

    public void setDiscordID(Integer discordID) {
        this.discordID = discordID;
    }
}
