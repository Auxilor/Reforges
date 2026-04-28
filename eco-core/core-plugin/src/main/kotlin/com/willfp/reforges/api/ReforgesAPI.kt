@file:JvmName("ReforgesAPI")

package com.willfp.reforges.api

import com.willfp.eco.core.price.ConfiguredPrice
import com.willfp.reforges.api.event.ReforgeApplyEvent
import com.willfp.reforges.reforges.Reforge
import com.willfp.reforges.util.reforge
import com.willfp.reforges.util.timesReforged
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Apply a reforge to an item, firing [ReforgeApplyEvent].
 * Returns false if the event was cancelled; true if the reforge was applied.
 */
fun Player.applyReforge(
    item: ItemStack,
    reforge: Reforge,
    price: ConfiguredPrice,
    usedStone: Boolean = false
): Boolean {
    val event = ReforgeApplyEvent(this, item, reforge, price, usedStone)
    Bukkit.getPluginManager().callEvent(event)

    if (event.isCancelled) return false

    event.price.pay(this)
    item.reforge = event.reforge
    item.timesReforged++
    event.reforge.runOnReforgeEffects(this, item)
    return true
}
