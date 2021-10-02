package com.willfp.reforges.reforges;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.reforges.ReforgesPlugin;
import com.willfp.reforges.reforges.util.ReforgeUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.function.Function;

public class ReforgeLookup {
    /**
     * All registered providers.
     */
    private static final Set<Function<Player, Collection<ItemStack>>> PROVIDERS = new HashSet<>();

    /**
     * Cached items.
     */
    private static final Map<UUID, Collection<ItemStack>> ITEM_CACHE = new WeakHashMap<>();

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
    public static void registerProvider(@NotNull final Function<Player, Collection<ItemStack>> provider) {
        PROVIDERS.add(provider);
    }

    /**
     * Provide ItemStacks.
     *
     * @param player The player.
     * @return The ItemStacks.
     */
    public static Set<ItemStack> provide(@NotNull final Player player) {
        if (ITEM_CACHE.containsKey(player.getUniqueId())) {
            return new HashSet<>(ITEM_CACHE.get(player.getUniqueId()));
        }

        Set<ItemStack> found = new HashSet<>();
        for (Function<Player, Collection<ItemStack>> provider : PROVIDERS) {
            found.addAll(provider.apply(player));
        }
        found.removeIf(Objects::isNull);

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
    public static Set<Reforge> provideReforges(@NotNull final Player player) {
        if (REFORGE_CACHE.containsKey(player.getUniqueId())) {
            return new HashSet<>(REFORGE_CACHE.get(player.getUniqueId()));
        }

        Set<Reforge> found = new HashSet<>();

        for (ItemStack itemStack : provide(player)) {
            if (itemStack == null) {
                continue;
            }

            Reforge reforge = ReforgeUtils.getReforge(itemStack);

            if (reforge == null) {
                continue;
            }

            found.add(reforge);
        }

        REFORGE_CACHE.put(player.getUniqueId(), found);
        PLUGIN.getScheduler().runLater(() -> REFORGE_CACHE.remove(player.getUniqueId()), 40);

        return found;
    }

    static {
        registerProvider(player -> Collections.singletonList(player.getInventory().getItemInMainHand()));
        registerProvider(player -> Collections.singletonList(player.getInventory().getItemInOffHand()));
        registerProvider(player -> Arrays.asList(player.getInventory().getArmorContents()));
    }
}
