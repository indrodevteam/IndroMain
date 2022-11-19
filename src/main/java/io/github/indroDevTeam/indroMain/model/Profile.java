package io.github.indroDevTeam.indroMain.model;

import io.github.indroDevTeam.indroMain.IndroMain;
import lombok.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "player_id")
    private UUID playerId;

    @Column(name = "rank_id")
    private String rankId;

    @Column(name = "level")
    private int level;

    @Column(name = "current_xp")
    private int currentXp;

    @Column(name = "next_xp")
    private int nextXp;

    private transient Rank rank;
    private transient LocalDateTime cooldownTime = null;
    private transient boolean teleportActive = false;

    public Profile(UUID playerId, Rank rank, int level, int currentXp, int nextXp) {
        this.playerId = playerId;
        this.rank = rank;
        this.rankId = rank.getId();
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

}
