package com.willfp.reforges.libreforge

import com.willfp.reforges.api.applyReforge
import com.willfp.reforges.reforges.ReforgeTargets
import com.willfp.reforges.util.getRandomReforge
import com.willfp.reforges.util.reforge
import wiki.ecohub.config.Config
import wiki.ecohub.config.emptyConfig
import wiki.ecohub.core.price.ConfiguredPrice
import wiki.ecohub.core.registry.NoCompileData
import wiki.ecohub.rules.effect.Effect
import wiki.ecohub.rules.trigger.TriggerData
import wiki.ecohub.rules.trigger.TriggerParameter

object EffectApplyRandomReforge : Effect<NoCompileData>("apply_random_reforge") {
    override val description = "Applies a random reforge to the item, avoiding the item's current reforge if it has one."

    override val categories = setOf("inventory")

    override val additionalInfo = listOf(
        "Does nothing if the item has no valid reforge targets."
    )

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
