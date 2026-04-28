package com.willfp.reforges.libreforge

import com.willfp.eco.core.config.emptyConfig
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.price.ConfiguredPrice
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.reforges.api.applyReforge
import com.willfp.reforges.reforges.Reforges

object EffectApplyReforge : Effect<NoCompileData>("apply_reforge") {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.ITEM
    )

    override val arguments = arguments {
        require("reforge", "You must specify the reforge to apply!")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val player = data.player ?: return false
        val reforge = Reforges.getByKey(config.getString("reforge")) ?: return false
        val item = data.item ?: player.inventory.itemInMainHand

        if (!reforge.canBeAppliedTo(item)) return false

        player.applyReforge(item, reforge, ConfiguredPrice.createOrFree(emptyConfig()))
        return true
    }
}
