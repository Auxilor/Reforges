package com.willfp.reforges.reforges

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.items.tag.CustomItemTag
import com.willfp.reforges.util.reforgeStone
import org.bukkit.inventory.ItemStack

class ReforgeStoneTag(plugin: EcoPlugin): CustomItemTag(plugin.createNamespacedKey("stone")) {
    override fun matches(p0: ItemStack): Boolean {
        return p0.reforgeStone != null
    }

    override fun getExampleItem(): ItemStack {
        return Reforges.values().random().stone
    }
}
