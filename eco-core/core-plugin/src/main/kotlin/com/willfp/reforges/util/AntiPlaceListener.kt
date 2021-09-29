package com.willfp.reforges.util

import com.willfp.reforges.reforges.util.ReforgeUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

class AntiPlaceListener : Listener {
    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (ReforgeUtils.getReforgeStone(event.itemInHand) != null) {
            event.isCancelled = true
            event.setBuild(false)
        }
    }
}