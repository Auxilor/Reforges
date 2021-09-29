package com.willfp.reforges.reforges.util;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.eco.core.config.interfaces.JSONConfig;
import com.willfp.eco.core.events.EntityDeathByEntityEvent;
import com.willfp.eco.core.events.PlayerJumpEvent;
import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.eco.core.integrations.mcmmo.McmmoManager;
import com.willfp.eco.util.ArrowUtils;
import com.willfp.eco.util.NumberUtils;
import com.willfp.reforges.reforges.Reforge;
import org.bukkit.block.Block;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class WatcherTriggers extends PluginDependent<EcoPlugin> implements Listener {
    /**
     * Create new listener for watcher events.
     *
     * @param plugin The plugin to link the events to.
     */
    public WatcherTriggers(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Called when an entity launches a projectile.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onProjectileLaunch(@NotNull final ProjectileLaunchEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getEntity() instanceof AbstractArrow)) {
            return;
        }

        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        LivingEntity shooter = (LivingEntity) event.getEntity().getShooter();

        Projectile projectile = event.getEntity();

        if (shooter.getEquipment() == null) {
            return;
        }

        ItemStack item = shooter.getEquipment().getItemInMainHand();

        if (projectile instanceof Trident trident) {
            item = trident.getItem();
        }

        Reforge reforge = ReforgeUtils.getReforge(item);

        if (reforge == null) {
            return;
        }

        for (Map.Entry<Effect, JSONConfig> entry : reforge.getEffects().entrySet()) {
            if (NumberUtils.randFloat(0, 100) > (entry.getValue().has("chance") ? entry.getValue().getDouble("chance") : 100)) {
                continue;
            }
            entry.getKey().onProjectileLaunch(shooter, projectile, event, entry.getValue());
        }
    }

    /**
     * Called when an entity takes fall damage.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onFallDamage(@NotNull final EntityDamageEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity victim)) {
            return;
        }

        EntityEquipment entityEquipment = victim.getEquipment();

        if (entityEquipment == null) {
            return;
        }

        for (ItemStack itemStack : entityEquipment.getArmorContents()) {
            if (itemStack == null) {
                continue;
            }

            Reforge reforge = ReforgeUtils.getReforge(itemStack);

            if (reforge == null) {
                continue;
            }


            for (Map.Entry<Effect, JSONConfig> entry : reforge.getEffects().entrySet()) {
                if (NumberUtils.randFloat(0, 100) > (entry.getValue().has("chance") ? entry.getValue().getDouble("chance") : 100)) {
                    continue;
                }
                entry.getKey().onFallDamage(victim, event, entry.getValue());
            }
        }
    }

    /**
     * Called when an arrow hits a block or entity.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onArrowHit(@NotNull final ProjectileHitEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getEntity().getShooter() instanceof LivingEntity shooter)) {
            return;
        }

        if (!(event.getEntity() instanceof Arrow arrow)) {
            return;
        }

        if (event.getEntity().getShooter() == null) {
            return;
        }

        ItemStack bow = ArrowUtils.getBow(arrow);

        if (bow == null) {
            return;
        }

        Reforge reforge = ReforgeUtils.getReforge(bow);

        if (reforge == null) {
            return;
        }


        for (Map.Entry<Effect, JSONConfig> entry : reforge.getEffects().entrySet()) {
            if (NumberUtils.randFloat(0, 100) > (entry.getValue().has("chance") ? entry.getValue().getDouble("chance") : 100)) {
                continue;
            }
            entry.getKey().onArrowHit(shooter, event, entry.getValue());
        }
    }

    /**
     * Called when a trident hits a block or entity.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onTridentHit(@NotNull final ProjectileHitEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getEntity().getShooter() instanceof LivingEntity shooter)) {
            return;
        }

        if (event.getEntity().getShooter() == null) {
            return;
        }

        if (!(event.getEntity() instanceof Trident trident)) {
            return;
        }

        ItemStack item = trident.getItem();

        Reforge reforge = ReforgeUtils.getReforge(item);

        if (reforge == null) {
            return;
        }

        for (Map.Entry<Effect, JSONConfig> entry : reforge.getEffects().entrySet()) {
            if (NumberUtils.randFloat(0, 100) > (entry.getValue().has("chance") ? entry.getValue().getDouble("chance") : 100)) {
                continue;
            }
            entry.getKey().onTridentHit(shooter, event, entry.getValue());
        }
    }

    /**
     * Called when an entity takes damage wearing armor.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onDamageWearingArmor(@NotNull final EntityDamageEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity victim)) {
            return;
        }

        EntityEquipment entityEquipment = victim.getEquipment();

        if (entityEquipment == null) {
            return;
        }

        for (ItemStack itemStack : entityEquipment.getArmorContents()) {
            if (itemStack == null) {
                continue;
            }

            Reforge reforge = ReforgeUtils.getReforge(itemStack);

            if (reforge == null) {
                continue;
            }

            for (Map.Entry<Effect, JSONConfig> entry : reforge.getEffects().entrySet()) {
                if (NumberUtils.randFloat(0, 100) > (entry.getValue().has("chance") ? entry.getValue().getDouble("chance") : 100)) {
                    continue;
                }
                entry.getKey().onDamageWearingArmor(victim, event, entry.getValue());
            }
        }
    }

    /**
     * Called when an entity throws a trident.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onTridentLaunch(@NotNull final ProjectileLaunchEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getEntity() instanceof Trident trident)) {
            return;
        }

        if (!(event.getEntity().getShooter() instanceof LivingEntity)) {
            return;
        }

        LivingEntity shooter = (LivingEntity) trident.getShooter();
        ItemStack item = trident.getItem();

        if (shooter == null) {
            return;
        }

        Reforge reforge = ReforgeUtils.getReforge(item);

        if (reforge == null) {
            return;
        }

        for (Map.Entry<Effect, JSONConfig> entry : reforge.getEffects().entrySet()) {
            if (NumberUtils.randFloat(0, 100) > (entry.getValue().has("chance") ? entry.getValue().getDouble("chance") : 100)) {
                continue;
            }

            entry.getKey().onTridentLaunch(shooter, trident, event, entry.getValue());
        }
    }
}
