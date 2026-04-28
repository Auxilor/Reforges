package com.willfp.reforges.api.event

import com.willfp.eco.core.price.ConfiguredPrice
import com.willfp.reforges.reforges.Reforge
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent
import org.bukkit.inventory.ItemStack

class ReforgeApplyEvent(
    who: Player,
    val item: ItemStack,
    override var reforge: Reforge,
    var price: ConfiguredPrice,
    val usedStone: Boolean
) : PlayerEvent(who), Cancellable, ReforgeEvent {
    private var cancelled = false

    override fun isCancelled() = cancelled

    override fun setCancelled(cancelled: Boolean) {
        this.cancelled = cancelled
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}
