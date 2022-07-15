package io.github.indroDevTeam.indroMain.ranks;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rank {
    private String id;
    private String name;
    private int maxHomes, teleportDelay, teleportCooldown; // rank config data
    private String tabPrefix, chatPrefix ; // rank display config

    public Rank(String id, String name, int maxHomes, int teleportDelay, int teleportCooldown, String tabPrefix,
            String chatPrefix) {
        this.id = id;
        this.name = name;
        this.maxHomes = maxHomes;
        this.teleportDelay = teleportDelay;
        this.teleportCooldown = teleportCooldown;
        this.tabPrefix = tabPrefix;
        this.chatPrefix = chatPrefix;
    }
}
