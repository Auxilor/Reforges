package com.willfp.reforges.reforges.util

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.integrations.antigrief.AntigriefManager
import com.willfp.eco.core.integrations.mcmmo.McmmoManager
import com.willfp.eco.util.ArrowUtils
import com.willfp.eco.util.NumberUtils
import org.bukkit.entity.Arrow
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent

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

}