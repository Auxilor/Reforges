package com.willfp.reforges.reforges

import com.willfp.reforges.util.reforge
import org.bukkit.inventory.ItemStack
import wiki.ecohub.rules.holder.slot.ItemHolderFinder
import wiki.ecohub.rules.holder.slot.SlotType

object ReforgeFinder : ItemHolderFinder<Reforge>() {
    override fun find(item: ItemStack): List<Reforge> {
        return listOfNotNull(item.reforge)
    }

    override fun isValidInSlot(holder: Reforge, slot: SlotType): Boolean {
        return holder.targets.map { it.slot }.any { it.isOrContains(slot) }
    }
}
