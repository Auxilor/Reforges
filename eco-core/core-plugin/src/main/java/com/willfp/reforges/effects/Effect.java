package com.willfp.reforges.effects;

import com.willfp.eco.core.config.interfaces.JSONConfig;
import com.willfp.reforges.ReforgesPlugin;
import com.willfp.reforges.reforges.util.Watcher;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public abstract class Effect implements Listener, Watcher {
    /**
     * Instance of reforges.
     */
    @Getter(AccessLevel.PROTECTED)
    private final ReforgesPlugin plugin = ReforgesPlugin.getInstance();

    /**
     * The effect ID.
     */
    private final String id;

    /**
     * Create a new effect.
     *
     * @param id The id.
     */
    protected Effect(@NotNull final String id) {
        this.id = id;
        Effects.addNewEffect(this);
    }

    /**
     * Get the effect ID.
     *
     * @return The ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Generate a UUID with a specified index.
     *
     * @param index The index.
     * @param extra The extra
     * @return The UUID.
     */
    protected UUID getUUID(final int index,
                           @NotNull final Object... extra) {
        return UUID.nameUUIDFromBytes((this.id + index + Arrays.hashCode(extra)).getBytes());
    }

    /**
     * Handle application of a reforge containing this effect.
     *
     * @param meta   The ItemMeta.
     * @param config The config.
     */
    public void handleEnabling(@NotNull final ItemMeta meta,
                               @NotNull final JSONConfig config) {
        // Override when needed
    }

    /**
     * Handle removal of a reforge containing this effect.
     *
     * @param meta The ItemMeta.
     */
    public void handleDisabling(@NotNull final ItemMeta meta) {
        // Override when needed
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Effect effect)) {
            return false;
        }
        return this.getId().equals(effect.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
