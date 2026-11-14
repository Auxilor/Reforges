package com.willfp.reforges.reforges

import com.willfp.reforges.plugin
import com.willfp.reforges.util.reforgeStone
import org.bukkit.inventory.ItemStack
import wiki.ecohub.game.item.lookup.CustomItemTag

object ReforgeStoneTag : CustomItemTag(plugin.createNamespacedKey("stone")) {
    override fun matches(p0: ItemStack): Boolean {
        return p0.reforgeStone != null
    }

    override fun getExampleItem(): ItemStack {
        return Reforges.values().random().stone
    }
}
