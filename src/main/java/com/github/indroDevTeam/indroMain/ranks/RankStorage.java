package com.github.indroDevTeam.indroMain.ranks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.github.indroDevTeam.indroMain.IndroMain;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@SuppressWarnings("unused")
public class RankStorage {
    // CRUD: Create, Read, Update, Delete

    private static ArrayList<Rank> ranks = new ArrayList<>();

    public static void createRank(String rankName,
                                  String format,
                                  @Nullable ArrayList<String> nextRank,
                                  int maxHomes,
                                  @Nullable Integer discordID,
                                  @Nullable ArrayList<String> nextAdvancement,
                                  @Nullable HashMap<String, Object> rankConfig) {
        Rank rank = new Rank(rankName, format, nextRank, maxHomes, discordID, nextAdvancement,
                rankConfig);
        ranks.add(rank);
    }

    public static Rank readRank(String rankName) {
        for (Rank rank : ranks) {
            if (rank.getRankTag().equalsIgnoreCase(rankName)) {
                return rank;
            }
        }
        return null;
    }

    /**
     * remember that this is an array (starts at 0)
     * @param rankPos the number of the rank position
     * @return null if the position doesn't exist, the rank saved if otherwise
     */
    public static Rank readRank(int rankPos) {
        try {
            return ranks.get(rankPos);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }


    public static void updateRank(String rankName, Rank newRank) {
        for (Rank rank : ranks) {
            if (rank.getRankTag().equalsIgnoreCase(rankName)) {
                rank.setRankTag(newRank.getRankTag());
                rank.setFormat(newRank.getFormat());
                rank.setRankConfig(newRank.getRankConfig());
                rank.setDiscordID(newRank.getDiscordID());
                rank.setNextRanks(newRank.getNextRanks());
                rank.setMaxHomes(newRank.getMaxHomes());
                rank.setAdvancementGate(newRank.getAdvancementGate());
                return;
            }
        }
    }

    public static void deleteRank(String rankName) {
        ranks.removeIf(rank -> rank.getRankTag().equalsIgnoreCase(rankName));
    }

    public static ArrayList<Rank> findAllRanks() {return ranks;}

    public static void saveRanks() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(IndroMain.getInstance().getDataFolder().getAbsolutePath() + File.separator + "ranks.json");
        if (!file.exists()) {
            file.getParentFile().mkdir();
            file.createNewFile();
        }

        Writer writer = new FileWriter(file, false);
        gson.toJson(ranks, writer);
        writer.flush();
        writer.close();
    }

    public static void loadRanks() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(IndroMain.getInstance().getDataFolder().getAbsolutePath() + File.separator + "ranks.json");
        if (!file.exists()) {
            loadFromResource();
            return;
        }
        Rank[] model = gson.fromJson(new FileReader(file), Rank[].class);
        ranks = new ArrayList<>(Arrays.asList(model));
    }

    private static void loadFromResource() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(IndroMain.getInstance().getDataFolder() + File.separator + "ranks.json");
        if (file.exists()) {return;}

        InputStream rankStream = IndroMain.getInstance().getResource("ranks.json");
        assert rankStream != null;
        InputStreamReader inputStreamReader = new InputStreamReader(rankStream);
        ranks = new ArrayList<>(Arrays.asList(gson.fromJson(inputStreamReader, Rank[].class)));
        saveRanks();
    }
}
