package com.willfp.reforges.reforges.util;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.util.ListUtils;
import com.willfp.reforges.ReforgesPlugin;
import com.willfp.reforges.conditions.ConfiguredCondition;
import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReforgeLookup {
    /**
     * All registered providers.
     */
    private static final Set<Function<Player, Map<ItemStack, ReforgeTarget.Slot>>> PROVIDERS = new HashSet<>();

    /**
     * Cached items.
     */
    private static final Map<UUID, Map<ItemStack, ReforgeTarget.Slot>> ITEM_CACHE = new WeakHashMap<>();

    /**
     * Cached reforges.
     */
    private static final Map<UUID, Collection<Reforge>> REFORGE_CACHE = new WeakHashMap<>();

    /**
     * Instance of Reforges.
     */
    private static final EcoPlugin PLUGIN = ReforgesPlugin.getInstance();

    /**
     * Register provider.
     *
     * @param provider The provider.
     */
    public static void registerProvider(@NotNull final Function<Player, Map<ItemStack, ReforgeTarget.Slot>> provider) {
        PROVIDERS.add(provider);
    }

    /**
     * Provide ItemStacks.
     *
     * @param player The player.
     * @return The ItemStacks.
     */
    public static Map<ItemStack, ReforgeTarget.Slot> provide(@NotNull final Player player) {
        if (ITEM_CACHE.containsKey(player.getUniqueId())) {
            return new HashMap<>(ITEM_CACHE.get(player.getUniqueId()));
        }

        Map<ItemStack, ReforgeTarget.Slot> found = new HashMap<>();
        for (Function<Player, Map<ItemStack, ReforgeTarget.Slot>> provider : PROVIDERS) {
            found.putAll(provider.apply(player));
        }
        found.keySet().removeIf(Objects::isNull);

        ITEM_CACHE.put(player.getUniqueId(), found);
        PLUGIN.getScheduler().runLater(() -> ITEM_CACHE.remove(player.getUniqueId()), 40);

        return found;
    }

    /**
     * Provide Reforges.
     *
     * @param player The player.
     * @return The Reforges.
     */
    public static List<Reforge> provideReforges(@NotNull final Player player) {
        if (REFORGE_CACHE.containsKey(player.getUniqueId())) {
            return new ArrayList<>(REFORGE_CACHE.get(player.getUniqueId()));
        }

        List<Reforge> found = new ArrayList<>();

        for (Map.Entry<ItemStack, ReforgeTarget.Slot> entry : provide(player).entrySet()) {
            ItemStack itemStack = entry.getKey();
            ReforgeTarget.Slot slot = entry.getValue();

            if (itemStack == null) {
                continue;
            }

            Reforge reforge = ReforgeUtils.getReforge(itemStack);

            if (reforge == null) {
                continue;
            }

            if (!reforge.getTargets().stream()
                    .map(ReforgeTarget::getSlot)
                    .collect(Collectors.toList())
                    .contains(slot)) {
                continue;
            }

            found.add(reforge);
        }

        REFORGE_CACHE.put(player.getUniqueId(), found);
        PLUGIN.getScheduler().runLater(() -> REFORGE_CACHE.remove(player.getUniqueId()), 40);

        return found;
    }

    /**
     * Clear caches.
     *
     * @param player The player.
     */
    public static void clearCaches(@NotNull final Player player) {
        REFORGE_CACHE.remove(player.getUniqueId());
        ITEM_CACHE.remove(player.getUniqueId());
    }

    /**
     * Update reforges for a player.
     *
     * @param player The player.
     */
    public static void updateReforges(@NotNull final Player player) {
        List<Reforge> before = ReforgeLookup.provideReforges(player);
        ReforgeLookup.clearCaches(player);
        PLUGIN.getScheduler().run(() -> {
            List<Reforge> after = ReforgeLookup.provideReforges(player);
            Map<Reforge, Integer> beforeFrequency = ListUtils.listToFrequencyMap(before);
            Map<Reforge, Integer> afterFrequency = ListUtils.listToFrequencyMap(after);

            List<Reforge> added = new ArrayList<>();
            List<Reforge> removed = new ArrayList<>();

            for (Map.Entry<Reforge, Integer> entry : new HashSet<>(afterFrequency.entrySet())) {
                int amount = entry.getValue();
                Reforge reforge = entry.getKey();

                amount -= beforeFrequency.getOrDefault(reforge, 0);

                if (amount < 1) {
                    continue;
                }

                for (int i = 0; i < amount; i++) {
                    added.add(reforge);
                }
            }

            for (Map.Entry<Reforge, Integer> entry : new HashSet<>(beforeFrequency.entrySet())) {
                int amount = entry.getValue();
                Reforge reforge = entry.getKey();

                amount -= afterFrequency.getOrDefault(reforge, 0);

                if (amount < 1) {
                    continue;
                }

                for (int i = 0; i < amount; i++) {
                    removed.add(reforge);
                }
            }

            for (Reforge reforge : added) {
                boolean areConditionsMet = true;
                for (ConfiguredCondition condition : reforge.getConditions()) {
                    if (!condition.getCondition().isConditionMet(player, condition.getConfig())) {
                        areConditionsMet = false;
                        break;
                    }
                }
                if (areConditionsMet) {
                    reforge.handleActivation(player);
                }
            }

            for (Reforge reforge : removed) {
                reforge.handleDeactivation(player);
            }

            for (Reforge reforge : after) {
                boolean areConditionsMet = true;
                for (ConfiguredCondition condition : reforge.getConditions()) {
                    if (!condition.getCondition().isConditionMet(player, condition.getConfig())) {
                        areConditionsMet = false;
                        break;
                    }
                }
                if (!areConditionsMet) {
                    reforge.handleDeactivation(player);
                }
            }
        });
    }

    static {
        registerProvider(player -> Map.of(
                player.getInventory().getItemInMainHand(),
                ReforgeTarget.Slot.HANDS
        ));
        registerProvider(player -> Map.of(
                player.getInventory().getItemInOffHand(),
                ReforgeTarget.Slot.HANDS
        ));
        registerProvider(player -> {
            Map<ItemStack, ReforgeTarget.Slot> items = new HashMap<>();
            for (ItemStack stack : player.getInventory().getArmorContents()) {
                items.put(stack, ReforgeTarget.Slot.ARMOR);
            }
            return items;
        });
    }
}
