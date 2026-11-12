package com.willfp.reforges.libreforge

import com.willfp.reforges.api.applyReforge
import com.willfp.reforges.reforges.Reforges
import wiki.ecohub.config.ArgType
import wiki.ecohub.config.Config
import wiki.ecohub.config.arguments
import wiki.ecohub.config.emptyConfig
import wiki.ecohub.core.price.ConfiguredPrice
import wiki.ecohub.core.registry.NoCompileData
import wiki.ecohub.rules.effect.Effect
import wiki.ecohub.rules.trigger.TriggerData
import wiki.ecohub.rules.trigger.TriggerParameter

object EffectApplyReforge : Effect<NoCompileData>("apply_reforge") {
    override val description = "Applies the specified reforge to the item, if the reforge can be applied to it."

    override val categories = setOf("inventory")

    override val additionalInfo = listOf(
        "Does nothing if the specified reforge cannot be applied to the item."
    )

    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.ITEM
    )

    override val arguments = arguments {
        require(
            "reforge",
            "You must specify the reforge to apply!",
            description = "The id of the reforge to apply to the item.",
            type = ArgType.STRING,
            example = "sharp"
        )
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
