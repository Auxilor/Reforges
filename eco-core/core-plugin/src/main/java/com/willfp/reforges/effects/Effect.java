package com.willfp.reforges.effects;

import com.willfp.eco.core.config.interfaces.JSONConfig;
import com.willfp.reforges.ReforgesPlugin;
import com.willfp.reforges.reforges.util.Watcher;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
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
    @Getter
    private final String id;

    /**
     * List of UUIDs.
     */
    private final Map<Integer, UUID> uuids;

    /**
     * Create a new effect.
     *
     * @param id The id.
     */
    protected Effect(@NotNull final String id) {
        this.id = id;
        this.uuids = new HashMap<>();
        Effects.addNewEffect(this);
    }

    /**
     * Generate a UUID with a specified index.
     *
     * @param index The index.
     * @return The UUID.
     */
    protected UUID getUUID(final int index) {
        if (!uuids.containsKey(index)) {
            uuids.put(index, UUID.nameUUIDFromBytes((this.id + index).getBytes()));
        }
        return uuids.get(index);
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
}
