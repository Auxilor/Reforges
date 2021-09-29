package com.willfp.reforges.effects;

import com.willfp.eco.core.config.interfaces.JSONConfig;
import com.willfp.reforges.ReforgesPlugin;
import com.willfp.reforges.reforges.util.Watcher;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class Effect implements Listener, Watcher {
    /**
     * Instance of Reforges.
     */
    @Getter(AccessLevel.PROTECTED)
    private final ReforgesPlugin plugin = ReforgesPlugin.getInstance();

    /**
     * The name of the effect.
     */
    @Getter
    private final String name;

    /**
     * The effect uuid.
     */
    @Getter
    private final UUID uuid;

    /**
     * Create a new effect.
     *
     * @param name The effect name.
     */
    protected Effect(@NotNull final String name) {
        this.name = name;
        this.uuid = UUID.nameUUIDFromBytes(name.getBytes());

        Effects.addNewEffect(this);
    }

    /**
     * Handle reforge application.
     *
     * @param meta   The meta.
     * @param config The config.
     */
    public void handleApplication(@NotNull final ItemMeta meta,
                                  @NotNull final JSONConfig config) {
        // Override when needed
    }

    /**
     * Handle reforge removal.
     *
     * @param meta The meta.
     */
    public void handleRemoval(@NotNull final ItemMeta meta) {
        // Override when needed
    }
}