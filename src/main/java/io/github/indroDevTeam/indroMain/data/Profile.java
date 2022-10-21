package io.github.indroDevTeam.indroMain.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.github.indroDevTeam.indroMain.IndroMain;

public class Profile implements Serializable {
    private UUID playerId;
    private List<Point> points;
    private int warpCap, warpDelay, warpCooldown, maxDistance;
    private boolean crossWorldPermitted;
    private transient LocalDateTime cooldownTime;
    private transient boolean teleportActive = false;

    public Profile() {}

    ///////////////////////////////////////////////////////////////////////////
    // Class-based Methods
    ///////////////////////////////////////////////////////////////////////////

    public void warp(Player player, @NotNull Point point) {
        if (teleportActive) {
            IndroMain.sendParsedMessage(player, ChatColor.BLUE + "A teleport is already queued up, please wait for that to complete before teleporting again.");
            return;
        }

        if (cooldownTime != null && LocalDateTime.now().isBefore(cooldownTime)) {
            IndroMain.sendParsedMessage(player, ChatColor.BLUE + "You've recently warped, wait " + (cooldownTime.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) + " seconds!");
            return;
        }

        // conditions have been fulfilled for a teleport
        IndroMain.sendParsedMessage(player, ChatColor.BLUE + "Teleporting...");
        Location location = point.getLocation();
        teleportActive = true;

        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(IndroMain.getInstance(), new Runnable() {
            private double radius = 0.25;
            private double angle = 360;

            @Override
            public void run() {
                for (int i = 0; i < 90; i++) {
                    double y = (radius * Math.sin(angle));
                    double z = (radius * Math.cos(angle));
                    angle -= 0.1;

                    DustOptions dustOptions = new DustOptions(Color.fromRGB(0, 0, 255), 0.5f);
                    player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(y, 0, z), 5, dustOptions);
                }
            }
        }, 0, 5);

        Bukkit.getScheduler().runTaskLater(IndroMain.getInstance(), () -> {
            Bukkit.getScheduler().cancelTask(id);
            if (player.getType().isAlive()) {
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
                location.getChunk().load();
                player.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
                player.teleport(location);
                IndroMain.sendParsedMessage(player, ChatColor.BLUE + "Teleport deployed!");
                return;
            }
            
            IndroMain.sendParsedMessage(player, ChatColor.YELLOW + "Teleport failed!"); 
            teleportActive = false;
        }, 20L * getWarpDelay());

        this.cooldownTime = LocalDateTime.now().plusSeconds(getWarpCooldown());
    }

    @Nullable
    public Point getPoint(String name) {
        for (Point p: points) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Getters and Setters
    ///////////////////////////////////////////////////////////////////////////
    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public int getWarpCap() {
        return warpCap;
    }

    public void setWarpCap(int warpCap) {
        this.warpCap = warpCap;
    }

    public int getWarpDelay() {
        return warpDelay;
    }

    public void setWarpDelay(int warpDelay) {
        this.warpDelay = warpDelay;
    }

    public int getWarpCooldown() {
        return warpCooldown;
    }

    public void setWarpCooldown(int warpCooldown) {
        this.warpCooldown = warpCooldown;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public boolean isCrossWorldPermitted() {
        return crossWorldPermitted;
    }

    public void setCrossWorldPermitted(boolean crossWorldPermitted) {
        this.crossWorldPermitted = crossWorldPermitted;
    }

    public LocalDateTime getCooldownTime() {
        return cooldownTime;
    }

    public void setCooldownTime(LocalDateTime cooldownTime) {
        this.cooldownTime = cooldownTime;
    }
}
