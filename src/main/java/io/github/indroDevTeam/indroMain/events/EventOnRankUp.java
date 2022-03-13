package io.github.indroDevTeam.indroMain.events;

import io.github.indroDevTeam.indroMain.ranks.Rank;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class EventOnRankUp extends Event{
    private final Player player;
    private final Rank oldRank;
    private final Rank newRank;

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public EventOnRankUp(Player player, Rank oldRank, Rank newRank) {
        this.player = player;
        this.oldRank = oldRank;
        this.newRank = newRank;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    public HandlerList getHandlerList() {
        return HANDLER_LIST;
    }


    public Player getPlayer() {
        return player;
    }

    public Rank getOldRank() {
        return oldRank;
    }

    public Rank getNewRank() {
        return newRank;
    }
}
