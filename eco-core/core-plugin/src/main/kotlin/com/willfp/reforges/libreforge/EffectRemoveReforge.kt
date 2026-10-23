package com.willfp.reforges.libreforge

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.reforges.util.reforge

object EffectRemoveReforge : Effect<NoCompileData>("remove_reforge") {
    override val description = "Removes any reforge applied to the triggering item, or the item in the player's main hand."

    override val categories = setOf("inventory")

    override val additionalInfo = listOf(
        "Uses the item from the trigger when there is one, otherwise the item in the player's main hand. Does nothing if that item is air."
    )

    override val parameters = setOf(
        TriggerParameter.PLAYER
    )

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val player = data.player ?: return false
        val item = data.item ?: player.inventory.itemInMainHand

        if (item.type.isAir) return false

        item.reforge = null
        return true
    }
}
