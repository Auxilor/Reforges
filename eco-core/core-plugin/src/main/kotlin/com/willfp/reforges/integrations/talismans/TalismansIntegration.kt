package com.willfp.reforges.integrations.talismans

import com.willfp.eco.core.integrations.Integration
import com.willfp.reforges.reforges.meta.ReforgeTarget
import com.willfp.reforges.reforges.util.ReforgeLookup
import com.willfp.talismans.talismans.util.TalismanChecks
import org.bukkit.inventory.ItemStack

object TalismansIntegration : Integration {
    @JvmStatic
    fun registerProvider() {
        ReforgeLookup.registerProvider { player ->
            val provided = mutableMapOf<ItemStack, ReforgeTarget.Slot>()
            for (itemStack in TalismanChecks.getTalismanItemsOnPlayer(player, true)) {
                provided[itemStack] = ReforgeTarget.Slot.ANY
            }

            provided
        }
    }

    override fun getPluginName(): String {
        return "Talismans"
    }
}