package com.willfp.reforges.reforges.meta;

import com.google.common.collect.ImmutableSet;
import com.willfp.eco.core.config.updating.ConfigUpdater;
import com.willfp.eco.core.items.Items;
import com.willfp.eco.core.items.TestableItem;
import com.willfp.eco.core.recipe.parts.EmptyTestableItem;
import com.willfp.reforges.ReforgesPlugin;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ReforgeTarget {
    /**
     * Target containing the materials from all other targets.
     */
    public static final ReforgeTarget ALL = new ReforgeTarget("all", new HashSet<>());

    /**
     * Melee weapons.
     */
    public static final ReforgeTarget MELEE = new ReforgeTarget("melee");

    /**
     * Armor.
     */
    public static final ReforgeTarget ARMOR = new ReforgeTarget("armor");

    /**
     * Trident.
     */
    public static final ReforgeTarget TRIDENT = new ReforgeTarget("trident");

    /**
     * Bows.
     */
    public static final ReforgeTarget BOW = new ReforgeTarget("bow");

    /**
     * Pickaxes.
     */
    public static final ReforgeTarget PICKAXE = new ReforgeTarget("pickaxe");

    /**
     * Axes.
     */
    public static final ReforgeTarget AXE = new ReforgeTarget("axe");

    /**
     * All registered targets.
     */
    private static final Set<ReforgeTarget> REGISTERED = new HashSet<>();

    static {
        REGISTERED.add(ALL);
        REGISTERED.add(MELEE);
        REGISTERED.add(ARMOR);
        REGISTERED.add(TRIDENT);
        REGISTERED.add(BOW);
        REGISTERED.add(PICKAXE);
        REGISTERED.add(AXE);
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
     * Create new target.
     *
     * @param name  The name of the target.
     * @param items The items for the target.
     */
    public ReforgeTarget(@NotNull final String name,
                         @NotNull final Set<TestableItem> items) {
        this.name = name;
        items.removeIf(item -> item instanceof EmptyTestableItem);
        this.items = items;
    }

    /**
     * Create new target.
     *
     * @param name The name of the target.
     */
    public ReforgeTarget(@NotNull final String name) {
        this.name = name;
        this.items = new HashSet<>();

        update();
    }

    /**
     * Update the configs.
     */
    public void update() {
        this.items.clear();
        this.items.addAll(ReforgesPlugin.getInstance().getConfigYml().getStrings("targets." + name, false).stream()
                .map(Items::lookup)
                .collect(Collectors.toSet()));
    }

    /**
     * Get ReforgeTarget matching name.
     *
     * @param name The name to search for.
     * @return The matching ReforgeTarget, or null if not found.
     */
    public static ReforgeTarget getByName(@NotNull final String name) {
        Optional<ReforgeTarget> matching = REGISTERED.stream().filter(rarity -> rarity.getName().equalsIgnoreCase(name)).findFirst();
        return matching.orElse(null);
    }

    /**
     * Get target from item.
     *
     * @param item The item.
     * @return The target.
     */
    @Nullable
    public static ReforgeTarget getForItem(@NotNull final ItemStack item) {
        Optional<ReforgeTarget> matching = REGISTERED.stream()
                .filter(rarity -> !rarity.getName().equalsIgnoreCase("all"))
                .filter(rarity -> rarity.getItems().stream().anyMatch(testableItem -> testableItem.matches(item))).findFirst();
        return matching.orElse(null);
    }

    /**
     * Update all targets.
     *
     * @param plugin Instance of Reforges.
     */
    @ConfigUpdater
    public static void update(@NotNull final ReforgesPlugin plugin) {
        ALL.items.clear();
        for (ReforgeTarget reforgeTarget : REGISTERED) {
            if (reforgeTarget.name.equals("all")) {
                continue;
            }

            reforgeTarget.update();
            ALL.items.addAll(reforgeTarget.items);
        }
    }

    /**
     * Get all targets.
     *
     * @return A set of all targets.
     */
    public static Set<ReforgeTarget> values() {
        return ImmutableSet.copyOf(REGISTERED);
    }
}
