package com.willfp.reforges.reforges.meta;

import com.google.common.collect.ImmutableSet;
import com.willfp.eco.core.config.updating.ConfigUpdater;
import com.willfp.eco.core.items.TestableItem;
import com.willfp.eco.core.recipe.parts.EmptyTestableItem;
import com.willfp.reforges.ReforgesPlugin;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ReforgeTarget {
    /**
     * Target containing the materials from all other targets.
     */
    public static final ReforgeTarget ALL = new ReforgeTarget("all", Slot.ANY, new HashSet<>());

    /**
     * All registered targets.
     */
    private static final Map<String, ReforgeTarget> REGISTERED = new HashMap<>();

    static {
        REGISTERED.put("all", ALL);
        update(ReforgesPlugin.getInstance());
    }

    /**
     * The name of the target.
     */
    @Getter
    private final String name;

    /**
     * The materials of the target.
     */
    @Getter
    private final Set<TestableItem> items;

    /**
     * The slot for the target.
     */
    @Getter
    private final Slot slot;

    /**
     * Create new target.
     *
     * @param name  The name of the target.
     * @param items The items for the target.
     */
    public ReforgeTarget(@NotNull final String name,
                         @NotNull final Slot slot,
                         @NotNull final Set<TestableItem> items) {
        this.name = name;
        items.removeIf(item -> item instanceof EmptyTestableItem);
        this.items = items;
        this.slot = slot;
    }

    /**
     * If an item matches the target.
     *
     * @param itemStack The ItemStack.
     * @return If matches.
     */
    public boolean matches(@NotNull final ItemStack itemStack) {
        for (TestableItem item : this.items) {
            if (item.matches(itemStack)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get ReforgeTarget matching name.
     *
     * @param name The name to search for.
     * @return The matching ReforgeTarget, or null if not found.
     */
    public static ReforgeTarget getByName(@NotNull final String name) {
        return REGISTERED.get(name);
    }

    /**
     * Get target from item.
     *
     * @param item The item.
     * @return The target.
     */
    public static List<ReforgeTarget> getForItem(@NotNull final ItemStack item) {
        return REGISTERED.values().stream()
                .filter(target -> !target.getName().equalsIgnoreCase("all"))
                .filter(target -> target.matches(item))
                .collect(Collectors.toList());
    }

    /**
     * Update all targets.
     *
     * @param plugin Instance of Reforges.
     */
    @ConfigUpdater
    public static void update(@NotNull final ReforgesPlugin plugin) {
        ALL.items.clear();
        for (String id : new ArrayList<>(REGISTERED.keySet())) {
            if (id.equalsIgnoreCase("all")) {
                continue;
            }

            REGISTERED.remove(id);
        }

        for (String id : plugin.getTargetYml().getTargets()) {
            ReforgeTarget target = new ReforgeTarget(
                    id,
                    plugin.getTargetYml().getSlot(id),
                    plugin.getTargetYml().getTargetItems(id)
            );
            REGISTERED.put(id, target);
            ALL.items.addAll(target.items);
        }
    }

    /**
     * Get all targets.
     *
     * @return A set of all targets.
     */
    public static Set<ReforgeTarget> values() {
        return ImmutableSet.copyOf(REGISTERED.values());
    }

    /**
     * Reforge slots.
     */
    public enum Slot {
        HANDS,
        ARMOR,
        ANY
    }
}
