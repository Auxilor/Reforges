package com.willfp.reforges.reforges.util;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.eco.core.events.PlayerJumpEvent;
import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.eco.core.integrations.mcmmo.McmmoManager;
import com.willfp.eco.util.ArrowUtils;
import com.willfp.reforges.reforges.Reforge;
import org.bukkit.block.Block;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
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
     * Called when a player breaks a block.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(@NotNull final BlockBreakEvent event) {

        if (McmmoManager.isFake(event)) {
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!AntigriefManager.canBreakBlock(player, block)) {
            return;
        }

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        Reforge reforge = ReforgeUtils.getReforge(itemStack);

        if (reforge == null) {
            return;
        }

        reforge.onBlockBreak(player, block, event);
    }

    /**
     * Called when an entity shoots another entity with an arrow.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onArrowDamage(@NotNull final EntityDamageByEntityEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getDamager() instanceof Arrow arrow)) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity victim)) {
            return;
        }

        if (arrow.getShooter() == null) {
            return;
        }

        if (!(arrow.getShooter() instanceof LivingEntity attacker)) {
            return;
        }

        if (attacker instanceof Player && !AntigriefManager.canInjure((Player) attacker, victim)) {
            return;
        }

        if (event.isCancelled()) {
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

        reforge.onArrowDamage(attacker, victim, arrow, event);
    }

    /**
     * Called when an entity damages another entity with a trident throw.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onTridentDamage(@NotNull final EntityDamageByEntityEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getDamager() instanceof Trident trident)) {
            return;
        }

        if (!(((Trident) event.getDamager()).getShooter() instanceof LivingEntity attacker)) {
            return;
        }

        if (((Trident) event.getDamager()).getShooter() == null) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity victim)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        ItemStack item = trident.getItem();

        if (attacker instanceof Player && !AntigriefManager.canInjure((Player) attacker, victim)) {
            return;
        }

        Reforge reforge = ReforgeUtils.getReforge(item);

        if (reforge == null) {
            return;
        }

        reforge.onTridentDamage(attacker, victim, trident, event);
    }

    /**
     * Called when a player jumps.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onJump(@NotNull final PlayerJumpEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        Player player = event.getPlayer();

        for (ItemStack itemStack : player.getInventory().getArmorContents()) {
            if (itemStack == null) {
                continue;
            }

            Reforge reforge = ReforgeUtils.getReforge(itemStack);

            if (reforge == null) {
                continue;
            }

            reforge.onJump(player, event);
        }
    }

    /**
     * Called when an entity attacks another entity with a melee attack.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onMeleeAttack(@NotNull final EntityDamageByEntityEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getDamager() instanceof LivingEntity attacker)) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity victim)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.THORNS) {
            return;
        }

        if (attacker instanceof Player && !AntigriefManager.canInjure((Player) attacker, victim)) {
            return;
        }

        EntityEquipment entityEquipment = attacker.getEquipment();

        if (entityEquipment == null) {
            return;
        }

        Reforge reforge = ReforgeUtils.getReforge(entityEquipment.getItemInMainHand());

        if (reforge == null) {
            return;
        }

        reforge.onMeleeAttack(attacker, victim, event);
    }

    /**
     * Called when an entity shoots a bow.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onBowShoot(@NotNull final EntityShootBowEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (event.getProjectile().getType() != EntityType.ARROW) {
            return;
        }

        LivingEntity shooter = event.getEntity();
        Arrow arrow = (Arrow) event.getProjectile();

        ItemStack bow = ArrowUtils.getBow(arrow);

        if (bow == null) {
            return;
        }

        Reforge reforge = ReforgeUtils.getReforge(bow);

        if (reforge == null) {
            return;
        }

        reforge.onBowShoot(shooter, arrow, event);
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

        reforge.onProjectileLaunch(shooter, projectile, event);
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

            reforge.onFallDamage(victim, event);
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

        reforge.onArrowHit(shooter, event);
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

        reforge.onTridentHit(shooter, event);
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

            reforge.onDamageWearingArmor(victim, event);
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

        reforge.onTridentLaunch(shooter, trident, event);
    }
}
