package io.github.indroDevTeam.indroMain.ranks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.indroDevTeam.indroMain.IndroMain;

import javax.annotation.Nullable;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class RankStorage {
    // CRUD: Create, Read, Update, Delete

    private static ArrayList<Rank> ranks = new ArrayList<>();

    public static Rank createRank(String rankName,
                                  String format,
                                  int maxHomes,
                                  @Nullable String nextRank,
                                  @Nullable Integer discordID,
                                  @Nullable String nextAdvancement) {
        Rank rank = new Rank(rankName, format, nextAdvancement, maxHomes, nextRank, discordID);
        ranks.add(rank);
        return rank;
    }

    public static Rank readRank(String rankName) {
        for (Rank rank : ranks) {
            if (rank.getRankName().equalsIgnoreCase(rankName)) {
                return rank;
            }
        }
        return null;
    }

    /**
     * remember that this is an array (starts at 0)
     * @param rankPos the number of the rank position
     * @return null if the position doesn't exist, the rank there if otherwise
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
            if (rank.getRankName().equalsIgnoreCase(rankName)) {
                rank.setRankName(newRank.getRankName());
                rank.setNextRank(newRank.getNextRank());
                rank.setFormat(newRank.getFormat());
                rank.setMaxHomes(newRank.getMaxHomes());
                rank.setDiscordID(newRank.getDiscordID());
                return;
            }
        }
    }

    public static void deleteRank(String rankName) {
        ranks.removeIf(rank -> rank.getRankName().equalsIgnoreCase(rankName));
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
            saveRanks();
        }
        Rank[] model = gson.fromJson(new FileReader(file), Rank[].class);
        ranks = new ArrayList<>(Arrays.asList(model));
    }

    public static void loadFromResource() throws IOException {
        File file = new File(IndroMain.getInstance().getDataFolder().getAbsolutePath() + File.separator + "ranks.json");
        if (!file.exists()) {
            file.getParentFile().mkdir();
            file.createNewFile();
            
            InputStream is = IndroMain.getInstance().getResource(file.getName());
            OutputStream os = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.close();
            is.close();
        }
    }
}
