package com.willfp.reforges.reforges;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableSet;
import com.willfp.eco.core.config.updating.ConfigUpdater;
import com.willfp.reforges.reforges.reforges.ReforgeDynamic;
import com.willfp.reforges.reforges.reforges.ReforgeLight;
import com.willfp.reforges.reforges.reforges.ReforgeSharp;
import com.willfp.reforges.reforges.reforges.ReforgeWise;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
@SuppressWarnings({"unused", "checkstyle:JavadocVariable"})
public class Reforges {
    public static final String CONFIG_LOCATION = "config.";

    private static final BiMap<String, Reforge> BY_KEY = HashBiMap.create();

    public static final Reforge SHARP = new ReforgeSharp();
    public static final Reforge LIGHT = new ReforgeLight();
    public static final Reforge WISE = new ReforgeWise();
    public static final Reforge DYNAMIC = new ReforgeDynamic();

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
     */
    @ConfigUpdater
    public static void update() {
        for (Reforge reforge : new HashSet<>(values())) {
            reforge.update();
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
        BY_KEY.remove(reforge.getKey());
        BY_KEY.put(reforge.getKey(), reforge);
    }

    /**
     * Remove {@link Reforge} from Reforges.
     *
     * @param reforge The {@link Reforge} to remove.
     */
    public static void removeReforge(@NotNull final Reforge reforge) {
        BY_KEY.remove(reforge.getKey());
    }
}
