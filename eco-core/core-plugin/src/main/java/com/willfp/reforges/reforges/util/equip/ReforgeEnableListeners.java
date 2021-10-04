package com.willfp.reforges.reforges.util.equip;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.reforges.effects.ConfiguredEffect;
import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.Reforges;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ReforgeEnableListeners extends PluginDependent<EcoPlugin> implements Listener {
    /**
     * Initialize new listeners and link them to a plugin.
     *
     * @param plugin The plugin to link to.
     */
    public ReforgeEnableListeners(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Called on item pickup.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onItemPickup(@NotNull final EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!ReforgeTarget.ALL.matches(event.getItem().getItemStack())) {
            return;
        }

        refreshPlayer(player);
    }

    /**
     * Called on player join.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onPlayerJoin(@NotNull final PlayerJoinEvent event) {
        refresh();
    }

    /**
     * Called on player leave.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onPlayerLeave(@NotNull final PlayerQuitEvent event) {
        refresh();

        Player player = event.getPlayer();

        for (Reforge value : Reforges.values()) {
            for (ConfiguredEffect effect : value.getEffects()) {
                effect.getEffect().handleDisabling(player);
            }
        }
    }

    /**
     * Called on item drop.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onInventoryDrop(@NotNull final PlayerDropItemEvent event) {
        if (!ReforgeTarget.ALL.matches(event.getItemDrop().getItemStack())) {
            return;
        }

        refreshPlayer(event.getPlayer(), event.getItemDrop().getItemStack());
    }

    @EventHandler
    public void onSwitchHands(@NotNull final PlayerSwapHandItemsEvent event) {
        refreshPlayer(event.getPlayer(), event.getPlayer().getInventory().getItemInOffHand());
    }

    /**
     * Called on inventory click.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onInventoryClick(@NotNull final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        refreshPlayer((Player) event.getWhoClicked());
    }

    /**
     * Force refresh all online players.
     * <p>
     * This is a very expensive method.
     */
    public void refresh() {
        this.getPlugin().getServer().getOnlinePlayers().forEach(this::refreshPlayer);
    }

    private void refreshPlayer(@NotNull final Player player,
                               @NotNull final ItemStack... extra) {
        SyncReforgeEnableTask.CHECK.accept(player);
    }
}