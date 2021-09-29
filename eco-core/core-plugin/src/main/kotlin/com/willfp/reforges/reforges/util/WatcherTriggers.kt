package com.willfp.reforges.reforges.util

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.events.EntityDeathByEntityEvent
import com.willfp.eco.core.events.PlayerJumpEvent
import com.willfp.eco.core.integrations.antigrief.AntigriefManager
import com.willfp.eco.core.integrations.mcmmo.McmmoManager
import com.willfp.eco.util.ArrowUtils
import com.willfp.eco.util.NumberUtils
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityShootBowEvent

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
        val itemStack = player.inventory.itemInMainHand
        val reforge = ReforgeUtils.getReforge(itemStack) ?: return
        for ((key, value) in reforge.effects) {
            if (NumberUtils.randFloat(0.0, 100.0) > value.getDoubleOrNull("chance") ?: 100.0) {
                continue
            }
            key.onBlockBreak(player, block, event, value)
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

        if (shooter !is LivingEntity) {
            return
        }

        if (shooter is Player && !AntigriefManager.canInjure(shooter, victim)) {
            return
        }

        if (event.isCancelled) {
            return
        }

        val bow = ArrowUtils.getBow(arrow) ?: return

        val reforge = ReforgeUtils.getReforge(bow) ?: return

        for ((effect, config) in reforge.effects) {
            if (NumberUtils.randFloat(0.0, 100.0) > config.getDoubleOrNull("chance") ?: 100.0) {
                continue
            }
            effect.onArrowDamage(shooter, victim, arrow, event, config)
            effect.onAnyDamage(shooter, victim, event, config)
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

        if (shooter !is LivingEntity) {
            return
        }

        val item = trident.item

        if (shooter is Player && !AntigriefManager.canInjure(shooter, victim)) {
            return
        }

        if (event.isCancelled) {
            return
        }

        val reforge = ReforgeUtils.getReforge(item) ?: return

        for ((effect, config) in reforge.effects) {
            if (NumberUtils.randFloat(0.0, 100.0) > config.getDoubleOrNull("chance") ?: 100.0) {
                continue
            }
            effect.onTridentDamage(shooter, victim, trident, event, config)
            effect.onAnyDamage(shooter, victim, event, config)
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onJump(event: PlayerJumpEvent) {
        if (McmmoManager.isFake(event)) {
            return
        }
        val player = event.player
        for (itemStack in player.inventory.armorContents) {
            if (itemStack == null) {
                continue
            }
            val reforge = ReforgeUtils.getReforge(itemStack) ?: continue
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

        if (attacker !is LivingEntity) {
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

        if (attacker is Player && !AntigriefManager.canInjure(attacker, victim)) {
            return
        }

        val equipment = attacker.equipment ?: return

        val reforge = ReforgeUtils.getReforge(equipment.itemInMainHand) ?: return

        for ((effect, config) in reforge.effects) {
            if (NumberUtils.randFloat(0.0, 100.0) > config.getDoubleOrNull("chance") ?: 100.0) {
                continue
            }
            effect.onMeleeAttack(attacker, victim, event, config)
            effect.onAnyDamage(attacker, victim, event, config)
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onKill(event: EntityDeathByEntityEvent) {
        if (McmmoManager.isFake(event)) {
            return
        }

        var killer: Any? = null
        if (event.killer is LivingEntity) {
            killer = event.killer
        } else if (event.killer is Projectile) {
            if ((event.killer as Projectile).shooter is LivingEntity) {
                killer = (event.killer as Projectile).shooter
            }
        }

        if (killer !is LivingEntity) {
            return
        }

        val victim = event.victim

        if (killer is Player && !AntigriefManager.canInjure(killer, victim)) {
            return
        }

        val equipment = killer.equipment ?: return

        val reforge = ReforgeUtils.getReforge(equipment.itemInMainHand) ?: return

        for ((effect, config) in reforge.effects) {
            if (NumberUtils.randFloat(0.0, 100.0) > config.getDoubleOrNull("chance") ?: 100.0) {
                continue
            }
            effect.onKill(killer, victim, event, config)
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onBowShoot(event: EntityShootBowEvent) {
        if (McmmoManager.isFake(event)) {
            return
        }
        if (event.projectile.type != EntityType.ARROW) {
            return
        }

        val shooter = event.entity

        val arrow = event.projectile as Arrow

        val bow = ArrowUtils.getBow(arrow) ?: return

        val reforge = ReforgeUtils.getReforge(bow) ?: return

        for ((effect, config) in reforge.effects) {
            if (NumberUtils.randFloat(0.0, 100.0) > config.getDoubleOrNull("chance") ?: 100.0) {
                continue
            }
            effect.onBowShoot(shooter, arrow, event, config)
        }
    }

}