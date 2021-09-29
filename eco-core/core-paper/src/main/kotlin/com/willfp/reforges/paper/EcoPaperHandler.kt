package com.willfp.reforges.paper

import com.willfp.reforges.ReforgesPlugin
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.inventory.ItemStack

class EcoPaperHandler(
    private val plugin: ReforgesPlugin
) : PaperHandler {
    private val serializer = LegacyComponentSerializer.builder()
        .hexColors()
        .useUnusualXRepeatedCharacterHexFormat()
        .character('§')
        .build()

    override fun getDisplayName(itemStack: ItemStack): String {
        return serializer.serialize(itemStack.displayName())
    }
}