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
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType

@Suppress("DEPRECATION")
class ReforgesDisplay(private val plugin: ReforgesPlugin) : DisplayModule(plugin, DisplayPriority.HIGHEST) {
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
        vararg args: Any
    ) {
        val target = ReforgeTarget.getForItem(itemStack)

        if (target.isEmpty() && itemStack.type != Material.PLAYER_HEAD) {
            // Letting player heads through here to add the stone check
            return
        }

        val meta = itemStack.itemMeta ?: return

        val fastItemStack = FastItemStack.wrap(itemStack)

        val lore = fastItemStack.lore

        val reforge = ReforgeUtils.getReforge(meta)

        val stone = ReforgeUtils.getReforgeStone(meta)

        if (stone == null && target.isEmpty()) {
            return
        }

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
            if (stone.config.has("stone.texture") && stone.config.getString("stone.texture").isNotEmpty()) {
                SkullUtils.setSkullTexture(
                    meta as SkullMeta,
                    stone.config.getString("stone.texture")
                )
            }
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
                val displayName = (meta.displayName() ?: Component.translatable(itemStack)).replaceText(replacement)
                meta.persistentDataContainer.set(
                    originalComponentKey,
                    PersistentDataType.STRING,
                    serializer.serialize(displayName)
                )
                val newName = StringUtils.toComponent("${reforge.name} ")
                    .decoration(TextDecoration.ITALIC, false).append(displayName)
                meta.displayName(newName)
            }
        }
        itemStack.itemMeta = meta
        fastItemStack.lore = lore
    }

    override fun revert(itemStack: ItemStack) {
        ReforgeTarget.getForItem(itemStack) ?: return

        val meta = itemStack.itemMeta ?: return

        if (plugin.configYml.getBool("reforge.display-in-name") && Prerequisite.HAS_PAPER.isMet) {
            val originalName =
                meta.persistentDataContainer.get(originalComponentKey, PersistentDataType.STRING) ?: return
            meta.persistentDataContainer.remove(originalComponentKey)
            meta.displayName(serializer.deserialize(originalName).replaceText(replacement))
        }

        itemStack.itemMeta = meta
    }
}