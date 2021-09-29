package com.willfp.reforges.display

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.display.Display
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.display.DisplayPriority
import com.willfp.eco.core.fast.FastItemStack
import com.willfp.eco.util.SkullUtils
import com.willfp.reforges.reforges.meta.ReforgeTarget
import com.willfp.reforges.reforges.util.ReforgeUtils
import org.apache.commons.lang.WordUtils
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

class ReforgesDisplay(plugin: EcoPlugin) : DisplayModule(plugin, DisplayPriority.HIGHEST) {
    override fun display(
        itemStack: ItemStack,
        vararg args: Any
    ) {
        val target = ReforgeTarget.getForItem(itemStack)

        if (target == null && itemStack.type != Material.PLAYER_HEAD) {
            return
        }

        val meta = itemStack.itemMeta ?: return

        val fastItemStack = FastItemStack.wrap(itemStack)

        val lore = fastItemStack.lore

        val reforge = ReforgeUtils.getReforge(meta)

        val stone = ReforgeUtils.getReforgeStone(meta)

        if (reforge == null && stone == null && target != null) {
            if (plugin.configYml.getBool("reforge.show-reforgable")) {
                val addLore: MutableList<String> = ArrayList()
                for (string in plugin.configYml.getStrings("reforge.reforgable-suffix")) {
                    addLore.add(Display.PREFIX + string)
                }
                lore.addAll(addLore)
            }
        }

        if (stone != null) {
            meta.setDisplayName(plugin.configYml.getString("reforge.stone.name").replace("%reforge%", stone.name))
            SkullUtils.setSkullTexture(
                meta as SkullMeta,
                SkullUtils.getSkullTexture(stone.stone.itemMeta as SkullMeta) ?: return
            )
            itemStack.itemMeta = meta
            val stoneLore = plugin.configYml.getStrings("reforge.stone.lore").map {
                "${Display.PREFIX}${it.replace("%reforge%", stone.name)}"
            }.toList()
            lore.addAll(0, stoneLore)
        }
        if (reforge != null) {
            if (plugin.configYml.getBool("reforge.display-in-lore")) {
                val addLore: MutableList<String> = ArrayList()
                addLore.add(" ")
                addLore.add(reforge.name)
                val description = mutableListOf(
                    *WordUtils.wrap(
                        reforge.description,
                        plugin.configYml.getInt("reforge.line-wrap"),
                        "\n",
                        false
                    ).split("\\r?\\n").toTypedArray()
                )
                description.replaceAll { s: String ->
                    plugin.langYml.getString("description-color") + s.replace(
                        "%description%",
                        reforge.description
                    )
                }
                description.replaceAll { s: String ->
                    s.replace(
                        "§r",
                        plugin.langYml.getString("description-color")
                    )
                }
                addLore.addAll(description)
                addLore.replaceAll { "${Display.PREFIX}$it" }
                lore.addAll(addLore)
            }
        }
        itemStack.itemMeta = meta
        fastItemStack.lore = lore
    }
}