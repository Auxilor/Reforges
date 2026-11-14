package com.willfp.reforges.reforges

import org.bukkit.inventory.ItemStack
import wiki.ecohub.game.item.lookup.EmptyTestableItem
import wiki.ecohub.game.item.lookup.TestableItem
import wiki.ecohub.rules.holder.slot.SlotType
import java.util.Objects

class ReforgeTarget(
    val id: String,
    val slot: SlotType,
    val items: MutableSet<TestableItem>
) {
    init {
        items.removeIf { it is EmptyTestableItem }
    }

    fun matches(itemStack: ItemStack): Boolean {
        for (item in items) {
            if (item.matches(itemStack)) {
                return true
            }
        }
        return false
    }

    override fun equals(other: Any?): Boolean {
        if (other !is ReforgeTarget) {
            return false
        }

        return this.id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hash(this.id)
    }
}
