package com.willfp.reforges.conditions.conditions

import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.reforges.conditions.Condition
import com.willfp.reforges.reforges.util.ReforgeLookup
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.FoodLevelChangeEvent


class ConditionBelowHungerPercent : Condition("below_hunger_percent") {
    @EventHandler(
        priority = EventPriority.MONITOR,
        ignoreCancelled = true
    )
    fun handle(event: FoodLevelChangeEvent) {
        val player = event.entity

        if (player !is Player) {
            return
        }

        ReforgeLookup.updateReforges(player)
    }

    override fun isConditionMet(player: Player, config: JSONConfig): Boolean {
        return (player.foodLevel / 20) * 100 <= config.getDouble("percent")
    }
}