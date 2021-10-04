package com.willfp.reforges.conditions.conditions

import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.reforges.conditions.Condition
import com.willfp.reforges.reforges.util.ReforgeLookup
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerToggleSneakEvent

class ConditionIsSneaking: Condition("is_sneaking") {
    @EventHandler(
        priority = EventPriority.MONITOR,
        ignoreCancelled = true
    )
    fun handle(event: PlayerToggleSneakEvent) {
        val player = event.player

        ReforgeLookup.updateReforges(player)
    }

    override fun isConditionMet(player: Player, config: JSONConfig): Boolean {
        return player.isSprinting
    }
}