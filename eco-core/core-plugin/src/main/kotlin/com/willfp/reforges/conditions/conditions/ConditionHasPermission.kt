package com.willfp.reforges.conditions.conditions

import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.reforges.conditions.Condition
import com.willfp.reforges.conditions.updateReforge
import com.willfp.reforges.reforges.ReforgeLookup
import org.bukkit.Bukkit
import org.bukkit.entity.Player


class ConditionHasPermission : Condition("is_sneaking"), Runnable {
    override fun run() {
        for (player in Bukkit.getOnlinePlayers()) {
            val items = ReforgeLookup.provide(player)
            for (item in items) {
                item.updateReforge(player, this)
            }
        }
    }

    override fun isConditionMet(player: Player, config: JSONConfig): Boolean {
        return player.hasPermission(config.getString("permission"))
    }
}