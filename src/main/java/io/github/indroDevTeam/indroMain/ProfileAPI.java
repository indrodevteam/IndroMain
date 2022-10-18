package io.github.indroDevTeam.indroMain;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.*;
import io.github.indroDevTeam.indroMain.data.Point;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import io.github.indroDevTeam.indroMain.data.Profile;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.naming.Name;

public class ProfileAPI {
    private LinkedList<Profile> list;
    
    public ProfileAPI() throws IOException, InvalidConfigurationException {
        this.list = new LinkedList<>();
        this.loadFromResource();
    }

    public static Profile createDefaultProfile(Player player) {
        Profile profile = new Profile();
        profile.setPlayerId(player.getUniqueId());
        profile.setPoints(new LinkedList<>());
        profile.setWarpCooldown(30);
        profile.setWarpCap(2);
        profile.setWarpDelay(10);
        profile.setCrossWorldPermitted(true);
        profile.setMaxDistance(500);

        return profile;
    }


    ///////////////////////////////////////////////////////////////////////////
    // Data Methods
    ///////////////////////////////////////////////////////////////////////////
    public void add(Profile profile) {
        if (!find(profile.getPlayerId())) {
            list.add(profile);
        } else {
            IndroMain.getInstance().getLogger().warning(profile.getPlayerId().toString() + " already exists!");
        }
    }

    public boolean find(UUID playerId) {
        for (Profile profile : list) {
            if (profile.getPlayerId().equals(playerId)) {
                return true;
            }
        }
        return false;
    }

    public void delete() {}

    @Nullable
    public Profile findProfile(UUID playerId) {
        for (Profile profile : list) {
            if (profile.getPlayerId().equals(playerId)) {
                return profile;
            }
        }
        return null;
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

    public LinkedList<Profile> list() {
        return list;
    }

    public void saveToResource() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(IndroMain.getInstance().getDataFolder().getAbsolutePath() + File.separator + "data.json");
        if (!file.exists()) {
            file.getParentFile().mkdir();
            file.createNewFile();
        }
        Writer writer = new FileWriter(file, false);
        gson.toJson(list, writer);
        writer.flush();
        writer.close();
    }

    public void loadFromResource() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(IndroMain.getInstance().getDataFolder().getAbsolutePath() + File.separator + "data.json");
        if (!file.exists()) {
            saveToResource();
        }
        Profile[] model = gson.fromJson(new FileReader(file), Profile[].class);
        this.list = new LinkedList<>(Arrays.asList(model));
    }
}
