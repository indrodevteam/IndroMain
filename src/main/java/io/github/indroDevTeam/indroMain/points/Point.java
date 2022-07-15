package io.github.indroDevTeam.indroMain.points;

import java.lang.reflect.Constructor;

import javax.annotation.Nonnull;
import javax.swing.text.WrappedPlainView;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.managers.ConfigManager;

public class Point {
    private String name;
    private String ownerUUID;
    private int locX, locY, locZ;
    private float locPitch, locYaw;
    private String locWorldName;
    private PointType type;

    public Point(String name, String ownerUUID,
            int locX, int locY, int locZ, float locPitch, float locYaw,
            String locWorldName, @NotNull PointType type) {
        this.name = name;
        this.ownerUUID = ownerUUID;
        this.locX = locX;
        this.locY = locY;
        this.locZ = locZ;
        this.locPitch = locPitch;
        this.locYaw = locYaw;
        this.locWorldName = locWorldName;
        this.type = type;
    }

    // Class Methods

    public Location getLocation() {
        World world = IndroMain.getInstance().getServer().getWorld(locWorldName);
        return new Location(world, locX, locY, locZ, locYaw, locPitch);
    }

    public void warp(Player player) {
        int maxDistance;
        final ConfigManager manager = IndroMain.getInstance().getSettingsManager();
        switch (type) {
            case PRIVATE_WARP ->
                maxDistance = manager.getPrivateWarpRange();
            default ->
                maxDistance = Integer.MAX_VALUE;
        }
        
        double distance = player.getLocation().distance(this.getLocation());

        if (maxDistance > (int) distance) {
            // cancel maps
            player.sendMessage(manager.getMessageWarpedDeniedDistance());
            return;
        }

        int delayWarpSeconds;
        delayWarpSeconds = (int) Double.parseDouble());
        
        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(IndroMain.getInstance(), new Runnable() {
            double var = 0;
            Location loc, first, second;

            @Override
            public void run() {
                var += Math.PI / 16;

                loc = player.getLocation();
                first = loc.clone().add(Math.cos(var), Math.sin(var) + 1, Math.sin(var));
                second = loc.clone().add(Math.cos(var + Math.PI), Math.sin(var) + 1, Math.sin(var + Math.PI));

                player.getWorld().spawnParticle(Particle.TOTEM, first, 0);
                player.getWorld().spawnParticle(Particle.TOTEM, second, 0);
            }
        }, 0, 1);

        final Location location = getLocation();

        Bukkit.getScheduler().runTaskLater(IndroMain.getInstance(), () -> {
            Bukkit.getScheduler().cancelTask(id);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
            location.getChunk().load();
            player.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
            player.teleport(location);
            player.sendMessage();
        }, 20L * delayWarpSeconds);
    }

    public enum PointType {
        PUBLIC_WARP,
        PRIVATE_WARP;
    }
}

