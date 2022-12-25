package io.github.indroDevTeam.indroMain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class Rank {
    /* Variables */
    private String name;
    private String chatTag;
    private String tabTag;

    // rank promotions
    private List<String> nextRanks;
    private List<String> advanceRequired;

    // warp data
    private int warpCap;
    private int warpDelay;
    private int warpCooldown;
    private int maxDistance;
    private boolean crossWorldPermitted;

    /* Constructors */
    public Rank(String name, String chatTag, String tabTag,
                List<String> nextRanks, List<String> advanceRequired,
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

    public Map<Advancement, Boolean> isPlayerPromotable(Player player) {
        Map<Advancement, Boolean> map = new HashMap<>();

        for (String s: advanceRequired) {
            Iterator<Advancement> it = Bukkit.getServer().advancementIterator();

            // gets all 'registered' advancements on the server.
            while (it.hasNext()) {
                // loops through these.
                Advancement a = it.next();
                if (a.getKey().getKey().equalsIgnoreCase(s)) {
                    map.put(a, player.getAdvancementProgress(a).isDone());
                    System.out.println(a.getKey().getKey());
                    break;
                }
            }
            System.out.println("Loop " + s);
        }
        return map;
    }
}
