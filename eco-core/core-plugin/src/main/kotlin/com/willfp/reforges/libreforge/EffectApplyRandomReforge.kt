package com.willfp.reforges.libreforge

import com.willfp.eco.core.config.emptyConfig
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.price.ConfiguredPrice
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.reforges.api.applyReforge
import com.willfp.reforges.reforges.ReforgeTargets
import com.willfp.reforges.util.getRandomReforge
import com.willfp.reforges.util.reforge

object EffectApplyRandomReforge : Effect<NoCompileData>("apply_random_reforge") {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.ITEM
    )

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val player = data.player ?: return false
        val item = data.item ?: player.inventory.itemInMainHand

        val targets = ReforgeTargets.getForItem(item)
        if (targets.isEmpty()) return false

        val reforge = targets.getRandomReforge(disallowed = listOfNotNull(item.reforge)) ?: return false

        player.applyReforge(item, reforge, ConfiguredPrice.createOrFree(emptyConfig()))
        return true
    }
}
