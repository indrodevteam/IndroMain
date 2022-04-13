package io.github.indroDevTeam.indroMain.ranks;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Rank implements ConfigurationSerializable {
    private String rankTag;
    private String format;
    private ArrayList<String> advancementGate;
    private int maxHomes;
    private Integer discordID;
    private ArrayList<String> nextRanks;
    private HashMap<String, Object> rankConfig;

    public Rank(String rankTag,
                String format,
                @Nullable ArrayList<String> advancementGate,
                int maxHomes,
                @Nullable Integer discordID,
                @Nullable ArrayList<String> nextRanks,
                @Nullable HashMap<String, Object> rankConfig) {
        this.rankTag = rankTag;
        this.format = format;
        this.advancementGate = advancementGate;
        this.maxHomes = maxHomes;
        this.discordID = discordID;
        this.nextRanks = nextRanks;
        if (rankConfig == null || rankConfig.isEmpty()) { //initialise rank config
            HashMap<String, Object> rankInit = new HashMap<>();
            for (RankConfigTags configTag: RankConfigTags.values()) {
                rankInit.put(configTag.toString(), configTag.defaultValue);
            }
            rankConfig = rankInit;
        }
        this.rankConfig = rankConfig;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("rankTag", this.rankTag);
        data.put("format", this.format);
        data.put("advancementGate", this.advancementGate);
        data.put("maxHomes", this.maxHomes);
        data.put("discordID", this.discordID);
        data.put("nextRanks", this.nextRanks);

        // rank configs...
        data.put("rankConfig", this.rankConfig);
        return data;
    }

    @NotNull
    public static Rank deserialize(Map<String, Object> data) {
        String rankTag = (String) data.get("rankTag");
        String format = (String) data.get("format");
        @Nullable ArrayList<String> advancementGate = (ArrayList<String>) data.getOrDefault("advancementGate", null);
        int maxHomes = (int) data.get("maxHomes");
        @Nullable Integer discordID = (Integer) data.getOrDefault("discordID", null);
        @Nullable ArrayList<String> nextRanks = (ArrayList<String>) data.getOrDefault("nextRanks", null);

        // here on in is the config tags
        HashMap<String, Object> rankConfig = (HashMap<String, Object>) data.getOrDefault("rankConfig", null);

        return new Rank(rankTag, format, advancementGate, maxHomes, discordID, nextRanks, rankConfig);
    }

    public Object getConfigTag(RankConfigTags tag) {
        Object obj = this.getRankConfig().get(tag.toString());
        Bukkit.getLogger().info(String.valueOf(obj));
        if (obj == null) obj = tag.defaultValue;
        return obj;
    }
}
