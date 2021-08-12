package com.willfp.reforges.reforges;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableSet;
import com.willfp.eco.core.config.updating.ConfigUpdater;
import com.willfp.reforges.reforges.reforges.ReforgeAerobic;
import com.willfp.reforges.reforges.reforges.ReforgeAffluent;
import com.willfp.reforges.reforges.reforges.ReforgeDynamic;
import com.willfp.reforges.reforges.reforges.ReforgeEvasive;
import com.willfp.reforges.reforges.reforges.ReforgeGravitated;
import com.willfp.reforges.reforges.reforges.ReforgeLight;
import com.willfp.reforges.reforges.reforges.ReforgeLucky;
import com.willfp.reforges.reforges.reforges.ReforgeNautical;
import com.willfp.reforges.reforges.reforges.ReforgePointy;
import com.willfp.reforges.reforges.reforges.ReforgeReinforced;
import com.willfp.reforges.reforges.reforges.ReforgeRich;
import com.willfp.reforges.reforges.reforges.ReforgeSharp;
import com.willfp.reforges.reforges.reforges.ReforgeStreamlined;
import com.willfp.reforges.reforges.reforges.ReforgeStrong;
import com.willfp.reforges.reforges.reforges.ReforgeThin;
import com.willfp.reforges.reforges.reforges.ReforgeTough;
import com.willfp.reforges.reforges.reforges.ReforgeVersatile;
import com.willfp.reforges.reforges.reforges.ReforgeWealthy;
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
    public static final Reforge GRAVITATED = new ReforgeGravitated();
    public static final Reforge STRONG = new ReforgeStrong();
    public static final Reforge WEALTHY = new ReforgeWealthy();
    public static final Reforge RICH = new ReforgeRich();
    public static final Reforge LUCKY = new ReforgeLucky();
    public static final Reforge AEROBIC = new ReforgeAerobic();
    public static final Reforge STREAMLINED = new ReforgeStreamlined();
    public static final Reforge THIN = new ReforgeThin();
    public static final Reforge TOUGH = new ReforgeTough();
    public static final Reforge REINFORCED = new ReforgeReinforced();
    public static final Reforge EVASIVE = new ReforgeEvasive();
    public static final Reforge NAUTICAL = new ReforgeNautical();
    public static final Reforge POINTY = new ReforgePointy();
    public static final Reforge VERSATILE = new ReforgeVersatile();
    public static final Reforge AFFLUENT = new ReforgeAffluent();

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
