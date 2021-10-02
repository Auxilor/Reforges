package com.willfp.reforges.conditions

import com.willfp.reforges.ReforgesPlugin
import com.willfp.reforges.reforges.util.ReforgeUtils
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

private val plugin = ReforgesPlugin.getInstance()

fun ItemStack.updateReforge(player: Player, condition: Condition) {
    plugin.scheduler.run {
        val meta = this.itemMeta ?: return@run

        val reforge = ReforgeUtils.getReforge(meta) ?: return@run

        var allow = true

        for ((cond, cfg) in reforge.conditions) {
            if (cond != condition) {
                continue
            }

            if (!cond.isConditionMet(player, cfg)) {
                allow = false
                break
            }
        }

        if (allow) {
            reforge.handleApplication(meta)
        } else {
            reforge.handleRemoval(meta)
        }

        this.itemMeta = meta
    }
}