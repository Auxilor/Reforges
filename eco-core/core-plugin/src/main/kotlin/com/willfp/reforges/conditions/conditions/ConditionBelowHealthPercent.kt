package com.willfp.reforges.conditions.conditions

import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.reforges.conditions.Condition
import com.willfp.reforges.conditions.updateReforge
import com.willfp.reforges.reforges.ReforgeLookup
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityRegainHealthEvent

class ConditionBelowHealthPercent: Condition("below_health_percent") {
    @EventHandler(
        priority = EventPriority.MONITOR,
        ignoreCancelled = true
    )
    fun handle(event: EntityRegainHealthEvent) {
        val player = event.entity

        if (player !is Player) {
            return
        }

        val items = ReforgeLookup.provide(player)

        for (item in items) {
            item.updateReforge(player, this)
        }
    }

    @EventHandler(
        priority = EventPriority.MONITOR,
        ignoreCancelled = true
    )
    fun handle(event: EntityDamageEvent) {
        val player = event.entity

        if (player !is Player) {
            return
        }

        val items = ReforgeLookup.provide(player)

        for (item in items) {
            item.updateReforge(player, this)
        }
    }

    override fun isConditionMet(player: Player, config: JSONConfig): Boolean {
        val maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: return false
        val health = player.health

        return health / maxHealth * 100 <= config.getDouble("percent")
    }
}