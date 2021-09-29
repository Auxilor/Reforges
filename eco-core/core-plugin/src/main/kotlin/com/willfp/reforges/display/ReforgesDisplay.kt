package com.willfp.reforges.display

import com.willfp.eco.core.display.Display
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.display.DisplayPriority
import com.willfp.eco.core.fast.FastItemStack
import com.willfp.eco.util.SkullUtils
import com.willfp.reforges.ReforgesPlugin
import com.willfp.reforges.reforges.meta.ReforgeTarget
import com.willfp.reforges.reforges.util.ReforgeUtils
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType

class ReforgesDisplay(private val plugin: ReforgesPlugin) : DisplayModule(plugin, DisplayPriority.HIGHEST) {
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
                addLore.addAll(reforge.description)
                addLore.replaceAll { "${Display.PREFIX}$it" }
                lore.addAll(addLore)
            }
            if (plugin.configYml.getBool("reforge.display-in-name")) {
                val displayName = plugin.paperHandler.getDisplayName(itemStack)
                val newName = "${reforge.name} ${ChatColor.RESET}${displayName}"
                meta.setDisplayName(newName)
                meta.persistentDataContainer.set(
                    plugin.namespacedKeyFactory.create("shadowed_name"),
                    PersistentDataType.STRING,
                    displayName
                )
            }
        }
        itemStack.itemMeta = meta
        fastItemStack.lore = lore
    }

    override fun revert(itemStack: ItemStack) {
        if (!plugin.configYml.getBool("reforge.display-in-name")) {
            return
        }


        val target = ReforgeTarget.getForItem(itemStack)

        if (target == null && itemStack.type != Material.PLAYER_HEAD) {
            return
        }

        val meta = itemStack.itemMeta ?: return
        val shadowed = meta.persistentDataContainer.get(
            plugin.namespacedKeyFactory.create("shadowed_name"),
            PersistentDataType.STRING
        ) ?: return
        meta.setDisplayName(shadowed)

        itemStack.itemMeta = meta
    }
}