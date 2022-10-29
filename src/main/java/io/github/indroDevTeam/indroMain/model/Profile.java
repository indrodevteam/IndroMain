package io.github.indrodevteam.indroMain.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import io.github.indrodevteam.indroMain.IndroMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Profile {
    protected UUID playerId;
    protected String rankId;
    protected int level, currentXp, nextXp;
    protected transient Rank rank;
    protected transient String playerName;
    protected transient LocalDateTime cooldownTime;
    protected transient boolean teleportActive = false;

    public Profile(UUID playerId, String rankId, int level, int currentXp, int nextXp) {
        this.playerId = playerId;
        this.rankId = rankId;
        this.level = level;
        this.currentXp = currentXp;
        this.nextXp = nextXp;
    }

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
            private double angle = 360;

            @Override
            public void run() {
                for (int i = 0; i < 90; i++) {
                    double radius = 0.5;
                    double y = (radius * Math.sin(angle));
                    double z = (radius * Math.cos(angle));
                    angle -= 0.1;

                    player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation().add(y, 2.5, z), 0, 0, -0.5, 0);
                }
            }
        }, 0, 10);

        Bukkit.getScheduler().runTaskLater(IndroMain.getInstance(), () -> {
            Bukkit.getScheduler().cancelTask(id);
            if (player.getType().isAlive()) {
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
                location.getChunk().load();
                player.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
                player.teleport(location);
                IndroMain.sendParsedMessage(player, ChatColor.BLUE + "Teleport deployed!");

                this.setCurrentXp(this.getCurrentXp() + 1);
                teleportActive = false;
                return;
            }
            
            IndroMain.sendParsedMessage(player, ChatColor.YELLOW + "Teleport failed!"); 
            teleportActive = false;
        }, 20L * rank.getWarpDelay());

        this.cooldownTime = LocalDateTime.now().plusSeconds(rank.getWarpCooldown());
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

    public String getRankId() {
        return rankId;
    }

    public void setRankId(String rankId) {
        this.rankId = rankId;
        //todo: get rank from RankDao
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCurrentXp() {
        return currentXp;
    }

    public void setCurrentXp(int currentXp) {
        this.currentXp = currentXp;
    }

    public int getNextXp() {
        return nextXp;
    }

    public void setNextXp(int nextXp) {
        this.nextXp = nextXp;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public LocalDateTime getCooldownTime() {
        return cooldownTime;
    }

    public void setCooldownTime(LocalDateTime cooldownTime) {
        this.cooldownTime = cooldownTime;
    }

    public boolean isTeleportActive() {
        return teleportActive;
    }

    public void setTeleportActive(boolean teleportActive) {
        this.teleportActive = teleportActive;
    }
}
