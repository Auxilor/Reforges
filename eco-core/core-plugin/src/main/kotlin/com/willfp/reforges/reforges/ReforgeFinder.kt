package com.willfp.reforges.reforges

import com.willfp.libreforge.slot.ItemHolderFinder
import com.willfp.libreforge.slot.SlotType
import com.willfp.reforges.util.reforge
import org.bukkit.inventory.ItemStack

object ReforgeFinder : ItemHolderFinder<Reforge>() {
    override fun find(item: ItemStack): List<Reforge> {
        return listOfNotNull(item.reforge)
    }

    override fun isValidInSlot(holder: Reforge, slot: SlotType): Boolean {
        return slot in holder.targets.map { it.slot }.toSet()
    }
}
