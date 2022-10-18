package io.github.indroDevTeam.indroMain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import io.github.indroDevTeam.indroMain.data.Profile;

public class ProfileAPI {
    public LinkedList<Profile> list;
    
    public ProfileAPI() throws FileNotFoundException, IOException, InvalidConfigurationException {
        list = new LinkedList<>();
        loadFromResource();
    }

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
        YamlConfiguration data = new YamlConfiguration();
        data.set("data", list);

        data.save(new File(IndroMain.getInstance().getDataFolder().getAbsolutePath() + File.separator + "data.yml"));
    }

    public void loadFromResource() throws FileNotFoundException, IOException, InvalidConfigurationException {
        File file = new File(IndroMain.getInstance().getDataFolder().getAbsolutePath() + File.separator + "data.yml");

        YamlConfiguration data = new YamlConfiguration();
        data.load(file);
        this.list = (LinkedList<Profile>) data.getList("data");
    }
}
