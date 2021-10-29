package com.willfp.reforges.events;

import com.willfp.reforges.effects.Effect;
import com.willfp.reforges.reforges.Reforge;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class EffectActivateEvent extends PlayerEvent implements Cancellable {

    private final Reforge reforge;

    private final Effect effect;

    private boolean cancelled;

    private static final HandlerList HANDLERS = new HandlerList();

    public EffectActivateEvent(@NotNull Player who, @NotNull Reforge reforge, @NotNull Effect effect) {
        super(who);
        this.reforge = reforge;
        this.effect = effect;
        this.cancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public Reforge getReforge() {
        return this.reforge;
    }

    @NotNull
    public Effect getEffect() {
        return this.effect;
    }

}
