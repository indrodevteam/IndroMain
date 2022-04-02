package io.github.indroDevTeam.indroMain.tasks;

import io.github.indroDevTeam.indroMain.dataUtils.LanguageTags;
import io.github.indroDevTeam.indroMain.ranks.RankStorage;
import io.github.indroDevTeam.indroMain.teleports.PointStorage;
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
            plugin.getLogger().info(LanguageTags.AUTOSAVING.get());
            PointStorage.savePoints();
            RankStorage.saveRanks();
        } catch (IOException e) {
            plugin.getLogger().severe("Data could not be saved!");
            e.printStackTrace();
        }
    }
}
