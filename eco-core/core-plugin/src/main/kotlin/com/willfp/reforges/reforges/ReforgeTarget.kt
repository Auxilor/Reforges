package com.willfp.reforges.reforges

import com.willfp.eco.core.items.TestableItem
import com.willfp.eco.core.recipe.parts.EmptyTestableItem
import org.bukkit.inventory.ItemStack
import java.util.*

class ReforgeTarget(
    val id: String,
    val slot: Slot,
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

    enum class Slot {
        HANDS,
        ARMOR,
        ANY
    }
}
