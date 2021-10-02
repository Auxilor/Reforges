package com.willfp.reforges.reforges;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableSet;
import com.willfp.eco.core.config.interfaces.JSONConfig;
import com.willfp.eco.core.config.updating.ConfigUpdater;
import com.willfp.reforges.ReforgesPlugin;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@UtilityClass
@SuppressWarnings("unused")
public class Reforges {
    private static final BiMap<String, Reforge> BY_KEY = HashBiMap.create();

    /**
     * Get all registered {@link Reforge}s.
     *
     * @return A list of all {@link Reforge}s.
     */
    public static Set<Reforge> values() {
        return ImmutableSet.copyOf(BY_KEY.values());
    }

    /**
     * Get {@link String}s for all registered {@link Reforge}s.
     *
     * @return A list of all {@link Reforge}s.
     */
    public static Set<String> keySet() {
        return ImmutableSet.copyOf(BY_KEY.keySet());
    }

    /**
     * Get {@link Reforge} matching key.
     *
     * @param key The key to search for.
     * @return The matching {@link Reforge}, or null if not found.
     */
    public static Reforge getByKey(@Nullable final String key) {
        if (key == null) {
            return null;
        }
        return BY_KEY.get(key);
    }

    /**
     * Update all {@link Reforge}s.
     *
     * @param plugin Instance of Reforges.
     */
    @ConfigUpdater
    public static void update(@NotNull final ReforgesPlugin plugin) {
        for (Reforge reforge : values()) {
            removeReforge(reforge);
        }

        for (JSONConfig config : plugin.getReforgesJson().getSubsections("reforges")) {
            addNewReforge(new Reforge(config, plugin));
        }
    }

    /**
     * Add new {@link Reforge} to Reforges.
     * <p>
     * Only for internal use, reforges are automatically added in the constructor.
     *
     * @param reforge The {@link Reforge} to add.
     */
    public static void addNewReforge(@NotNull final Reforge reforge) {
        BY_KEY.remove(reforge.getId());
        BY_KEY.put(reforge.getId(), reforge);
    }

    /**
     * Remove {@link Reforge} from Reforges.
     *
     * @param reforge The {@link Reforge} to remove.
     */
    public static void removeReforge(@NotNull final Reforge reforge) {
        BY_KEY.remove(reforge.getId());
    }
}
