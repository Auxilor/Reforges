package com.willfp.reforges.reforges

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.items.tag.CustomItemTag
import com.willfp.reforges.util.reforge
import com.willfp.reforges.util.reforgeStone
import org.bukkit.inventory.ItemStack

class ReforgedTag(plugin: EcoPlugin): CustomItemTag(plugin.createNamespacedKey("reforged")) {
    override fun matches(p0: ItemStack): Boolean {
        return p0.reforge != null
    }
}
