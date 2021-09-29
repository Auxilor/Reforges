package com.willfp.reforges.paper

import org.bukkit.inventory.ItemStack

interface PaperHandler {
    fun getDisplayName(itemStack: ItemStack): String
}