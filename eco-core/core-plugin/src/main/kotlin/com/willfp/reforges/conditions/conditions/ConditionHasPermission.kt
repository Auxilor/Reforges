package com.willfp.reforges.conditions.conditions

import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.reforges.conditions.Condition
import org.bukkit.entity.Player


class ConditionHasPermission : Condition("is_sneaking") {
    override fun isConditionMet(player: Player, config: JSONConfig): Boolean {
        return player.hasPermission(config.getString("permission"))
    }
}