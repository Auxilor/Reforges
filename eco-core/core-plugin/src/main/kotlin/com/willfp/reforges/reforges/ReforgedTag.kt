package com.willfp.reforges.reforges

import com.willfp.eco.core.items.tag.CustomItemTag
import com.willfp.reforges.plugin
import com.willfp.reforges.util.reforge
import org.bukkit.inventory.ItemStack

object ReforgedTag : CustomItemTag(plugin.createNamespacedKey("reforged")) {
    override fun matches(p0: ItemStack): Boolean {
        return p0.reforge != null
    }
}
