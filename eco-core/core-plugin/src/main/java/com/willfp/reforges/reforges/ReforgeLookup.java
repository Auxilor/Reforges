package com.willfp.reforges.reforges;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.reforges.ReforgesPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
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
    private static final Map<UUID, Collection<ItemStack>> CACHE = new WeakHashMap<>();

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
        if (CACHE.containsKey(player.getUniqueId())) {
            return new HashSet<>(CACHE.get(player.getUniqueId()));
        }

        Set<ItemStack> found = new HashSet<>();
        for (Function<Player, Collection<ItemStack>> provider : PROVIDERS) {
            found.addAll(provider.apply(player));
        }

        CACHE.put(player.getUniqueId(), found);
        PLUGIN.getScheduler().runLater(() -> CACHE.remove(player.getUniqueId()), 40);

        return found;
    }

    static {
        registerProvider(player -> Collections.singletonList(player.getInventory().getItemInMainHand()));
        registerProvider(player -> Collections.singletonList(player.getInventory().getItemInOffHand()));
        registerProvider(player -> Arrays.asList(player.getInventory().getArmorContents()));
    }
}
