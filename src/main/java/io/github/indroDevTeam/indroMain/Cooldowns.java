package io.github.indroDevTeam.indroMain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

public class Cooldowns {
    private HashMap<UUID, LocalDateTime> cooldownList;
    
    public Cooldowns() {
        cooldownList = new HashMap<>();
        new BukkitRunnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Set<UUID> pList = cooldownList.keySet();
                for (UUID uuid : pList) {
                    if (cooldownList.get(uuid).isAfter(LocalDateTime.now())) {
                        cooldownList.remove(uuid);
                    }
                }
            }
        }.runTaskTimer(IndroMain.getInstance(), 200, 10);
    }

    public void addCooldown(UUID playerUuid, int delaySeconds) {

    }
}
