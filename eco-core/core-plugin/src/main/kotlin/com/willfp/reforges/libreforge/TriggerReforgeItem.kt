package com.willfp.reforges.libreforge

import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.reforges.api.event.ReforgeApplyEvent
import com.willfp.reforges.api.extensions.round
import org.bukkit.event.EventHandler

object TriggerReforgeItem : Trigger("reforge_item") {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.ITEM,
        TriggerParameter.LOCATION,
        TriggerParameter.TEXT,
        TriggerParameter.VALUE,
        TriggerParameter.EVENT
    )

    @EventHandler(ignoreCancelled = true)
    fun handle(event: ReforgeApplyEvent) {
        val player = event.player

        this.dispatch(
            player.toDispatcher(),
            TriggerData(
                player = player,
                item = event.item,
                location = player.location,
                text = event.reforge.name,
                value = event.price.getValue(player).round(2),
                event = event
            )
        )
    }
}