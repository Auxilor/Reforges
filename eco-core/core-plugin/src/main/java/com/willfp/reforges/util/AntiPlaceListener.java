
package com.willfp.reforges.util;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.reforges.reforges.util.ReforgeUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class AntiPlaceListener extends PluginDependent<EcoPlugin> implements Listener {
    /**
     * Register listener.
     *
     * @param plugin Talismans.
     */
    public AntiPlaceListener(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Prevent block place.
     *
     * @param event The event.
     */
    @EventHandler
    public void onBlockPlace(@NotNull final BlockPlaceEvent event) {
        if (ReforgeUtils.getReforgeStone(event.getItemInHand()) != null) {
            event.setCancelled(true);
            event.setBuild(false);
        }
    }
}