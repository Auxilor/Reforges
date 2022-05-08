package com.willfp.reforges.display

import com.willfp.eco.core.display.Display
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.display.DisplayPriority
import com.willfp.eco.core.fast.FastItemStack
import com.willfp.eco.core.fast.fast
import com.willfp.eco.util.SkullUtils
import com.willfp.eco.util.StringUtils
import com.willfp.reforges.ReforgesPlugin
import com.willfp.reforges.reforges.meta.ReforgeTarget
import com.willfp.reforges.reforges.util.ReforgeUtils
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType

@Suppress("DEPRECATION")
class ReforgesDisplay(private val plugin: ReforgesPlugin) : DisplayModule(plugin, DisplayPriority.HIGH) {
    /**
     * Deprecated
     */
    @Deprecated("Use PDC components!")
    private val replacement = TextReplacementConfig.builder()
        .match("§w(.+)§w")
        .replacement("")
        .build()

    private val originalComponentKey = plugin.namespacedKeyFactory.create("real_name")
    private val serializer = GsonComponentSerializer.gson()

    override fun display(
        itemStack: ItemStack,
        player: Player?,
        vararg args: Any
    ) {
        val target = ReforgeTarget.getForItem(itemStack)

        val fast = itemStack.fast()

        val stone = ReforgeUtils.getReforgeStone(fast.persistentDataContainer)

        if (target.isEmpty() && stone == null) {
            return
        }

        val fastItemStack = FastItemStack.wrap(itemStack)

        val lore = fastItemStack.lore

        val reforge = ReforgeUtils.getReforge(fast.persistentDataContainer)

        if (reforge == null && stone == null && target != null) {
            if (plugin.configYml.getBool("reforge.show-reforgable")) {
                if (player != null && plugin.configYml.getBool("reforge.no-reforgable-in-gui")) {
                    val inventory = player.openInventory.topInventory
                    if (inventory.contents.contains(itemStack) && inventory.holder == null) {
                        return
                    }
                }

                val addLore: MutableList<String> = ArrayList()
                for (string in plugin.configYml.getFormattedStrings("reforge.reforgable-suffix")) {
                    addLore.add(Display.PREFIX + string)
                }
                lore.addAll(addLore)
            }
        }

        if (stone != null) {
            val meta = itemStack.itemMeta
            meta.setDisplayName(stone.config.getFormattedString("stone.name"))
            val stoneMeta = stone.stone.itemMeta
            if (stoneMeta is SkullMeta) {
                val stoneTexture = SkullUtils.getSkullTexture(stoneMeta)

                if (stoneTexture != null) {
                    try {
                        SkullUtils.setSkullTexture(meta as SkullMeta, stoneTexture)
                    } catch (e: StringIndexOutOfBoundsException) {
                        // Do nothing
                    }
                }
            }
            itemStack.itemMeta = meta
            val stoneLore = stone.config.getFormattedStrings("stone.lore").map {
                "${Display.PREFIX}$it"
            }.toList()
            lore.addAll(0, stoneLore)
        }

        if (reforge != null) {
            if (plugin.configYml.getBool("reforge.display-in-lore")) {
                val addLore: MutableList<String> = ArrayList()
                for (string in plugin.configYml.getFormattedStrings("reforge.reforged-prefix")) {
                    addLore.add(Display.PREFIX + string.replace("%reforge%", reforge.name))
                }
                addLore.addAll(reforge.description)
                addLore.replaceAll { "${Display.PREFIX}$it" }
                lore.addAll(addLore)
            }
            if (plugin.configYml.getBool("reforge.display-in-name")) {
                val displayName = fastItemStack.displayNameComponent.replaceText(replacement)

                val newName = StringUtils.toComponent("${reforge.name} ")
                    .decoration(TextDecoration.ITALIC, false).append(displayName)

                fastItemStack.setDisplayName(newName)

                fastItemStack.persistentDataContainer.set(
                    originalComponentKey,
                    PersistentDataType.STRING,
                    serializer.serialize(displayName)
                )
            }


            if (player != null) {
                val lines = reforge.getNotMetLines(player)

                if (lines.isNotEmpty()) {
                    lore.add("")
                    lore.addAll(lines)
                }
            }
        }

        fastItemStack.lore = lore
    }

    override fun revert(itemStack: ItemStack) {
        ReforgeTarget.getForItem(itemStack) ?: return

        val fis = FastItemStack.wrap(itemStack)

        if (plugin.configYml.getBool("reforge.display-in-name")) {
            val originalName =
                fis.persistentDataContainer.get(originalComponentKey, PersistentDataType.STRING) ?: return
            fis.persistentDataContainer.remove(originalComponentKey)
            fis.setDisplayName(serializer.deserialize(originalName).replaceText(replacement))
        }
    }
}
