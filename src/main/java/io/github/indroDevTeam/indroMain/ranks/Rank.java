package io.github.indroDevTeam.indroMain.ranks;

import javax.annotation.Nullable;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Rank implements Serializable {

    //default serialVersion id
    @Serial
    private static final long serialVersionUID = 1L;

    private String rankName;
    private String format;
    private String nextAdvancement;
    private int maxHomes;
    private String nextRank;
    private Integer discordID;

    public Rank(String rankName, String format, @Nullable String nextAdvancement, int maxHomes, @Nullable String nextRank, @Nullable Integer discordID) {
        this.rankName = rankName;
        this.format = format;
        this.nextAdvancement = nextAdvancement;
        this.maxHomes = maxHomes;
        this.nextRank = nextRank;
        this.discordID = discordID;
    }


    // getters and setters
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rank rank = (Rank) o;

        if (maxHomes != rank.maxHomes) return false;
        if (!rankName.equals(rank.rankName)) return false;
        if (!format.equals(rank.format)) return false;
        if (!Objects.equals(nextAdvancement, rank.nextAdvancement))
            return false;
        if (!Objects.equals(nextRank, rank.nextRank)) return false;
        return Objects.equals(discordID, rank.discordID);
    }

    @Override
    public int hashCode() {
        int result = rankName.hashCode();
        result = 31 * result + format.hashCode();
        result = 31 * result + (nextAdvancement != null ? nextAdvancement.hashCode() : 0);
        result = 31 * result + maxHomes;
        result = 31 * result + (nextRank != null ? nextRank.hashCode() : 0);
        result = 31 * result + (discordID != null ? discordID.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Rank{" +
                "rankName='" + rankName + '\'' +
                ", format='" + format + '\'' +
                ", nextAdvancement='" + nextAdvancement + '\'' +
                ", maxHomes=" + maxHomes +
                ", nextRank='" + nextRank + '\'' +
                ", discordID=" + discordID +
                '}';
    }
}
