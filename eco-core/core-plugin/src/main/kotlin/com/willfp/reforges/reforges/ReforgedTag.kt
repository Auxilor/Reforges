package com.willfp.reforges.reforges

import com.willfp.reforges.plugin
import com.willfp.reforges.util.reforge
import org.bukkit.inventory.ItemStack
import wiki.ecohub.game.item.lookup.CustomItemTag

object ReforgedTag : CustomItemTag(plugin.createNamespacedKey("reforged")) {
    override fun matches(p0: ItemStack): Boolean {
        return p0.reforge != null
    }
}
