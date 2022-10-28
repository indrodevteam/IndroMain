package io.github.indrodevteam.indroMain.data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;

import io.github.indrodevteam.indroMain.IndroMain;
import io.github.indrodevteam.indroMain.Point;
import io.github.indrodevteam.indroMain.Profile;
import io.github.indrodevteam.indroMain.Rank;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ProfileAPI {
    private LinkedList<Profile> profiles;
    private LinkedList<Point> points;
    private LinkedList<Rank> ranks;
    
    public ProfileAPI() throws IOException, InvalidConfigurationException {
        this.profiles = new LinkedList<>();
        this.loadFromResource();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Data Methods
    ///////////////////////////////////////////////////////////////////////////
    public void add(Profile profile) {
        if (!find(profile.getPlayerId())) {
            profiles.add(profile);
        } else {
            IndroMain.getInstance().getLogger().warning(profile.getPlayerId().toString() + " already exists!");
        }
    }

    public boolean find(UUID playerId) {
        for (Profile profile : profiles) {
            if (profile.getPlayerId().equals(playerId)) {
                return true;
            }
        }
        return false;
    }

    public void delete() {}

    @Nullable
    public Profile findProfile(UUID playerId) {
        for (Profile profile : profiles) {
            if (profile.getPlayerId().equals(playerId)) {
                return profile;
            }
        }

        IndroMain.getProfileAPI().add(ProfileAPI.createDefaultProfile(playerId));
        return IndroMain.getProfileAPI().findProfile(playerId);
    }

    
    public void update(UUID playerId, Profile newProfile) {
        if (find(playerId)) {
            Profile profile = findProfile(playerId);

            profile.setPoints(newProfile.getPoints());
            profile.setWarpCap(newProfile.getWarpCap());
            profile.setWarpCooldown(newProfile.getWarpCooldown());
            profile.setWarpDelay(newProfile.getWarpDelay());
        } else {
            IndroMain.getInstance().getLogger().warning(newProfile.getPlayerId().toString() + " does not exist!");
        }
    }
    public void saveToResource() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        File file = new File(IndroMain.getInstance().getDataFolder().getAbsolutePath() + File.separator + "profiles.json");
        if (!file.exists()) {
            file.getParentFile().mkdir();
            file.createNewFile();
        }
        Writer writer = new FileWriter(file, false);
        gson.toJson(profiles, writer);
        writer.flush();
        writer.close();

        file = new File(IndroMain.getInstance().getDataFolder().getAbsolutePath() + File.separator + "ranks.json");
        if (!file.exists()) {
            file.getParentFile().mkdir();
            file.createNewFile();
        }
        writer = new FileWriter(file, false);
        gson.toJson(ranks, writer);
        writer.flush();
        writer.close();


        file = new File(IndroMain.getInstance().getDataFolder().getAbsolutePath() + File.separator + "points.json");
        if (!file.exists()) {
            file.getParentFile().mkdir();
            file.createNewFile();
        }
        writer = new FileWriter(file, false);
        gson.toJson(points, writer);
        writer.flush();
        writer.close();
    }

    public void loadFromResource() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        File file = new File(IndroMain.getInstance().getDataFolder().getAbsolutePath() + File.separator + "profiles.json");
        if (!file.exists()) saveToResource();
        Profile[] profileModel = gson.fromJson(new FileReader(file), Profile[].class);
        this.profiles = new LinkedList<>(Arrays.asList(profileModel));

        file = new File(IndroMain.getInstance().getDataFolder().getAbsolutePath() + File.separator + "ranks.json");
        if (!file.exists()) saveToResource();
        Rank[] rankModel = gson.fromJson(new FileReader(file), Rank[].class);
        this.ranks = new LinkedList<>(Arrays.asList(rankModel));


    }
}
