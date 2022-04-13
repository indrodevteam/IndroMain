package com.github.indrodevteam.indroMain.tasks;

import com.github.indrodevteam.indroMain.ranks.RankStorage;
import com.github.indrodevteam.indroMain.teleports.PointStorage;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class TaskAutoSave extends BukkitRunnable {
    private final JavaPlugin plugin;

    public TaskAutoSave(JavaPlugin plugin) {
            this.plugin = plugin;
    }

    @Override
    public void run() {
        try {
            PointStorage.savePoints();
            RankStorage.saveRanks();
        } catch (IOException e) {
            plugin.getLogger().severe("Data could not be saved!");
            e.printStackTrace();
        }
    }
}
