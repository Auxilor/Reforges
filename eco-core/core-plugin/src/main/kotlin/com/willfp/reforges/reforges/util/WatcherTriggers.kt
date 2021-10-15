package com.willfp.reforges.reforges.util

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.events.EntityDeathByEntityEvent
import com.willfp.eco.core.events.PlayerJumpEvent
import com.willfp.eco.core.integrations.antigrief.AntigriefManager
import com.willfp.eco.core.integrations.mcmmo.McmmoManager
import com.willfp.eco.util.NumberUtils
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerItemDamageEvent

class WatcherTriggers(
    private val plugin: EcoPlugin
) : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        if (McmmoManager.isFake(event)) {
            return
        }

        val player = event.player
        val block = event.block
        if (!AntigriefManager.canBreakBlock(player, block)) {
            return
        }

        for (reforge in ReforgeLookup.provideReforges(player)) {
            for ((key, value) in reforge.effects) {
                if (NumberUtils.randFloat(0.0, 100.0) > value.getDoubleOrNull("chance") ?: 100.0) {
                    continue
                }
                key.onBlockBreak(player, block, event, value)
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onArrowDamage(event: EntityDamageByEntityEvent) {
        if (McmmoManager.isFake(event)) {
            return
        }

        val arrow = event.damager
        val victim = event.entity

        if (arrow !is Arrow) {
            return
        }

        if (victim !is LivingEntity) {
            return
        }

        val shooter = arrow.shooter

        if (shooter !is Player) {
            return
        }

        if (!AntigriefManager.canInjure(shooter, victim)) {
            return
        }

        if (event.isCancelled) {
            return
        }

        for (reforge in ReforgeLookup.provideReforges(shooter)) {
            for ((effect, config) in reforge.effects) {
                if (NumberUtils.randFloat(0.0, 100.0) > config.getDoubleOrNull("chance") ?: 100.0) {
                    continue
                }
                effect.onArrowDamage(shooter, victim, arrow, event, config)
                effect.onAnyDamage(shooter, victim, event, config)
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onTridentDamage(event: EntityDamageByEntityEvent) {
        if (McmmoManager.isFake(event)) {
            return
        }

        val trident = event.damager
        val victim = event.entity

        if (trident !is Trident) {
            return
        }

        if (victim !is LivingEntity) {
            return
        }

        val shooter = trident.shooter

        if (shooter !is Player) {
            return
        }

        val item = trident.item

        if (!AntigriefManager.canInjure(shooter, victim)) {
            return
        }

        if (event.isCancelled) {
            return
        }

        val tridentReforge = ReforgeUtils.getReforge(item)
        val add = if (tridentReforge == null) emptyList() else listOf(tridentReforge)

        for (reforge in ReforgeLookup.provideReforges(shooter) union add) {
            for ((effect, config) in reforge.effects) {
                if (NumberUtils.randFloat(0.0, 100.0) > config.getDoubleOrNull("chance") ?: 100.0) {
                    continue
                }
                effect.onTridentDamage(shooter, victim, trident, event, config)
                effect.onAnyDamage(shooter, victim, event, config)
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onJump(event: PlayerJumpEvent) {
        if (McmmoManager.isFake(event)) {
            return
        }
        val player = event.player

        for (reforge in ReforgeLookup.provideReforges(player)) {
            for ((effect, config) in reforge.effects) {
                if (NumberUtils.randFloat(0.0, 100.0) > config.getDoubleOrNull("chance") ?: 100.0) {
                    continue
                }
                effect.onJump(player, event, config)
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onMeleeAttack(event: EntityDamageByEntityEvent) {
        if (McmmoManager.isFake(event)) {
            return
        }

        val attacker = event.damager

        if (attacker !is Player) {
            return
        }

        val victim = event.entity

        if (victim !is LivingEntity) {
            return
        }

        if (event.isCancelled) {
            return
        }

        if (event.cause == EntityDamageEvent.DamageCause.THORNS) {
            return
        }

        if (!AntigriefManager.canInjure(attacker, victim)) {
            return
        }

        for (reforge in ReforgeLookup.provideReforges(attacker)) {
            for ((effect, config) in reforge.effects) {
                if (NumberUtils.randFloat(0.0, 100.0) > config.getDoubleOrNull("chance") ?: 100.0) {
                    continue
                }
                effect.onMeleeAttack(attacker, victim, event, config)
                effect.onAnyDamage(attacker, victim, event, config)
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onKill(event: EntityDeathByEntityEvent) {
        if (McmmoManager.isFake(event)) {
            return
        }

        var killer: Any? = null
        if (event.killer is Player) {
            killer = event.killer
        } else if (event.killer is Projectile) {
            if ((event.killer as Projectile).shooter is Player) {
                killer = (event.killer as Projectile).shooter
            }
        }

        if (killer !is Player) {
            return
        }

        val victim = event.victim

        if (!AntigriefManager.canInjure(killer, victim)) {
            return
        }

        val trident = if (event.killer is Trident) (event.killer as Trident).item else null
        val tridentReforge = if (trident == null) null else ReforgeUtils.getReforge(trident)
        val add = if (tridentReforge == null) emptyList() else listOf(tridentReforge)

        for (reforge in ReforgeLookup.provideReforges(killer) union add) {
            for ((effect, config) in reforge.effects) {
                if (NumberUtils.randFloat(0.0, 100.0) > config.getDoubleOrNull("chance") ?: 100.0) {
                    continue
                }
                (effect as Watcher).onKill(killer, victim, event, config)
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onProjectileLaunch(event: ProjectileLaunchEvent) {
        if (McmmoManager.isFake(event)) {
            return
        }

        val shooter = event.entity.shooter

        if (shooter !is Player) {
            return
        }

        for (reforge in ReforgeLookup.provideReforges(shooter)) {
            for ((effect, config) in reforge.effects) {
                if (NumberUtils.randFloat(0.0, 100.0) > config.getDoubleOrNull("chance") ?: 100.0) {
                    continue
                }
                effect.onProjectileLaunch(shooter, event.entity, event, config)
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onFallDamage(event: EntityDamageEvent) {
        if (McmmoManager.isFake(event)) {
            return
        }

        if (event.cause != EntityDamageEvent.DamageCause.FALL) {
            return
        }

        val victim = event.entity

        if (victim !is Player) {
            return
        }

        for (reforge in ReforgeLookup.provideReforges(victim)) {
            for ((effect, config) in reforge.effects) {
                if (NumberUtils.randFloat(0.0, 100.0) > config.getDoubleOrNull("chance") ?: 100.0) {
                    continue
                }
                effect.onFallDamage(victim, event, config)
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onProjectileHit(event: ProjectileHitEvent) {
        if (McmmoManager.isFake(event)) {
            return
        }

        val projectile = event.entity
        val shooter = projectile.shooter

        if (shooter !is Player) {
            return
        }

        val trident = if (projectile is Trident) projectile.item else null
        val tridentReforge = if (trident == null) null else ReforgeUtils.getReforge(trident)
        val add = if (tridentReforge == null) emptyList() else listOf(tridentReforge)

        for (reforge in ReforgeLookup.provideReforges(shooter) union add) {
            for ((effect, config) in reforge.effects) {
                if (NumberUtils.randFloat(0.0, 100.0) > config.getDoubleOrNull("chance") ?: 100.0) {
                    continue
                }
                effect.onProjectileHit(shooter, event, config)
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onDurabilityDamage(event: PlayerItemDamageEvent) {
        if (McmmoManager.isFake(event)) {
            return
        }

        val item = event.item
        val reforge = ReforgeUtils.getReforge(item) ?: return

        for ((effect, config) in reforge.effects) {
            if (NumberUtils.randFloat(0.0, 100.0) > config.getDoubleOrNull("chance") ?: 100.0) {
                continue
            }
            effect.onDurabilityDamage(event, config)
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onDamageWearingArmor(event: EntityDamageEvent) {
        if (McmmoManager.isFake(event)) {
            return
        }

        val victim = event.entity

        if (victim !is Player) {
            return
        }

        for (reforge in ReforgeLookup.provideReforges(victim)) {
            for ((effect, config) in reforge.effects) {
                if (NumberUtils.randFloat(0.0, 100.0) > config.getDoubleOrNull("chance") ?: 100.0) {
                    continue
                }
                effect.onDamageWearingArmor(victim, event, config)
            }
        }
    }
}