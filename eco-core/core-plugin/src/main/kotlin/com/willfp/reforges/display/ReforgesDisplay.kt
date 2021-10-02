package com.willfp.reforges.display

import com.willfp.eco.core.Prerequisite
import com.willfp.eco.core.display.Display
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.display.DisplayPriority
import com.willfp.eco.core.fast.FastItemStack
import com.willfp.eco.util.SkullUtils
import com.willfp.eco.util.StringUtils
import com.willfp.reforges.ReforgesPlugin
import com.willfp.reforges.reforges.meta.ReforgeTarget
import com.willfp.reforges.reforges.util.ReforgeUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

@Suppress("DEPRECATION")
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
                stone.config.getString("stone.texture")
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
            if (plugin.configYml.getBool("reforge.display-in-name") && Prerequisite.HAS_PAPER.isMet) {
                val displayName =
                    (if (meta.hasDisplayName()) meta.displayName()!! else Component.translatable(itemStack))
                        .replaceText(
                            TextReplacementConfig.builder()
                                .match("§w(.)§w")
                                .replacement("")
                                .build()
                        )
                val newName = StringUtils.toComponent("§w${reforge.name} §w")
                    .decoration(TextDecoration.ITALIC, false).append(displayName)
                meta.displayName(newName)
            }
        }
        itemStack.itemMeta = meta
        fastItemStack.lore = lore
    }

    override fun revert(itemStack: ItemStack) {
        val target = ReforgeTarget.getForItem(itemStack)

        if (target == null && itemStack.type != Material.PLAYER_HEAD) {
            return
        }

        val meta = itemStack.itemMeta ?: return

        if (plugin.configYml.getBool("reforge.display-in-name") && Prerequisite.HAS_PAPER.isMet) {
            val displayName = meta.displayName() ?: return
            meta.displayName(
                displayName.replaceText(
                    TextReplacementConfig.builder()
                        .match("§w(.)§w")
                        .replacement("")
                        .build()
                )
            )
        }

        itemStack.itemMeta = meta
    }
}