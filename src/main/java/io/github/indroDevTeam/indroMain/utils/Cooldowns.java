package io.github.indroDevTeam.indroMain.utils;

import io.github.indroDevTeam.indroMain.model.Profile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cooldowns {
    private final Map<UUID, LocalDateTime> previousWarps;
    private final Map<UUID, Boolean> teleportActive;

    public Cooldowns() {
        previousWarps = new HashMap<>();
        teleportActive = new HashMap<>();
    }

    public boolean isPlayerTeleportable(@NotNull Profile profile) {
        for (UUID uuid: previousWarps.keySet()) {
            if (uuid.equals(profile.getUserId())) {
                return previousWarps.get(uuid).isAfter(LocalDateTime.now());
            }
        }
        return false;
    }

    public int getRemainingTime(@NotNull Profile profile) {
        for (UUID uuid: previousWarps.keySet()) {
            if (uuid.equals(profile.getUserId())) {
                return (int) (previousWarps.get(uuid).toEpochSecond(ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
            }
        }
        return 0;
    }

    public void addTeleportCooldown(@NotNull Profile profile) {
        for (UUID uuid: previousWarps.keySet()) {
            if (uuid.equals(profile.getUserId())) {
                previousWarps.remove(uuid);
            }
        }
        previousWarps.put(profile.getUserId(), LocalDateTime.now().plusSeconds(profile.getRank().getWarpCooldown()));
    }

    public boolean checkTeleportStatus(Player player) {
        for (UUID uuid: teleportActive.keySet()) {
            if (uuid.equals(player.getUniqueId())) return teleportActive.get(player.getUniqueId());
        }
        teleportActive.put(player.getUniqueId(), false);
        return false;
    }
    public void setTeleportStatus(Player player, boolean status) {
        for (UUID uuid: teleportActive.keySet()) {
            if (uuid.equals(player.getUniqueId())) teleportActive.remove(uuid);
        }
        teleportActive.put(player.getUniqueId(), status);
    }
}
