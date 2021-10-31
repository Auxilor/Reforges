package com.willfp.reforges.events;

import com.willfp.reforges.effects.Effect;
import com.willfp.reforges.reforges.Reforge;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class EffectActivateEvent extends PlayerEvent implements Cancellable {
    /**
     * The reforge.
     */
    private final Reforge reforge;

    /**
     * The effect that activated.
     */
    private final Effect effect;

    /**
     * If the event is cancelled.
     */
    private boolean cancelled;

    /**
     * Bukkit parity.
     */
    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Create new EffectActivateEvent.
     *
     * @param who     The player.
     * @param reforge The reforge.
     * @param effect  The effect.
     */
    public EffectActivateEvent(@NotNull final Player who,
                               @NotNull final Reforge reforge,
                               @NotNull final Effect effect) {
        super(who);
        this.reforge = reforge;
        this.effect = effect;
        this.cancelled = false;
    }

    /**
     * Get if the effect activation is cancelled.
     *
     * @return If cancelled.
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Set if the event is cancelled.
     *
     * @param cancel If cancelled.
     */
    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Bukkit parity.
     *
     * @return The handlers.
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Get the reforge associated with the event.
     *
     * @return The reforge.
     */
    @NotNull
    public Reforge getReforge() {
        return this.reforge;
    }

    /**
     * Get the effect associated with the event.
     *
     * @return The effect.
     */
    @NotNull
    public Effect getEffect() {
        return this.effect;
    }

    /**
     * Bukkit parity.
     *
     * @return The handler list.
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
