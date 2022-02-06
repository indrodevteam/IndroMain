package indrocraft.indrocraftplugin.events;

import indrocraft.indrocraftplugin.utils.RankUtils;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServerRankEvent extends Event implements Cancellable {

    RankUtils rankUtils = new RankUtils();

    private final Player player;
    private final String rank;
    private final Advancement advancement;
    private boolean isCanceled;
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public ServerRankEvent(Player player, Advancement advancement, String rank) {
        this.player = player;
        this.rank = rank;
        this.advancement = advancement;
        this.isCanceled = false;
    }

    public ServerRankEvent(Player player, String advancement, String rank) {
        this.player = player;
        this.rank = rank;
        this.advancement = rankUtils.getAdvancement("minecraft:" + advancement);
        this.isCanceled = false;
    }

    @Override
    public boolean isCancelled() {
        return this.isCanceled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCanceled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Player getPlayer() {
        return player;
    }

    public Advancement getAdvancement() {
        return advancement;
    }

    public String getRank() {
        return rank;
    }
}
