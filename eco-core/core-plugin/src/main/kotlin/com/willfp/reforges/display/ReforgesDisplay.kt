package com.willfp.reforges.display

import com.willfp.eco.core.display.DisplayContext
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.display.DisplayPriority
import com.willfp.eco.core.fast.FastItemStack
import com.willfp.eco.core.fast.fast
import com.willfp.eco.util.SkullUtils
import com.willfp.eco.util.StringUtils
import com.willfp.eco.util.formatEcoRich
import com.willfp.eco.util.toJSON
import com.willfp.libreforge.ItemProvidedHolder
import com.willfp.reforges.plugin
import com.willfp.reforges.reforges.ReforgeTargets
import com.willfp.reforges.util.reforge
import com.willfp.reforges.util.reforgeStone
import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType

@Suppress("DEPRECATION")
object ReforgesDisplay : DisplayModule(plugin, DisplayPriority.HIGH) {
    private val tempKey = plugin.namespacedKeyFactory.create("temp")

    override fun display(context: DisplayContext) {
        val itemStack = context.itemStack
        val player = context.player
        val targets = ReforgeTargets.getForItem(itemStack)
        val fast = itemStack.fast()

        val stone = fast.persistentDataContainer.reforgeStone

        if (targets.isEmpty() && stone == null) {
            return
        }

        val reforge = fast.persistentDataContainer.reforge

        if (reforge == null && stone == null) {
            if (plugin.configYml.getBool("reforge.show-reforgable")) {
                if (context.properties.inGui) {
                    return
                }

                context.lore.append(plugin.configYml.getStrings("reforge.reforgable-suffix").formatEcoRich())
            }
        }

        if (stone != null) {
            val meta = itemStack.itemMeta
            meta.setDisplayName(stone.config.getFormattedString("stone.name"))
            val stoneMeta = stone.stone.itemMeta
            if (stoneMeta is SkullMeta) {
                val stoneTexture = SkullUtils.getSkullTexture(stoneMeta)

                if (stoneTexture != null) {
                    SkullUtils.setSkullTexture(meta as SkullMeta, stoneTexture)
                }
            }
            itemStack.itemMeta = meta

            context.lore.prepend(
                stone.config.getStrings("stone.lore")
                    .map { it.replace("%price%", if (player == null) "" else stone.stonePrice?.getDisplay(player) ?: "") }
                    .formatEcoRich()
            )
        }

        if (reforge != null) {
            if (plugin.configYml.getBool("reforge.display-in-lore")) {
                context.lore.append(
                    plugin.configYml.getStrings("reforge.reforged-prefix")
                        .map { it.replace("%reforge%", reforge.name) }
                        .formatEcoRich() + reforge.description.formatEcoRich(context.placeholderContext)
                )
            }

            if (plugin.configYml.getBool("reforge.display-in-name")) {
                val displayName = fast.displayNameComponent

                if (!fast.displayName.contains(reforge.name)) {
                    fast.persistentDataContainer.set(
                        tempKey,
                        PersistentDataType.STRING,
                        displayName.toJSON()
                    )

                    fast.setDisplayName(reforge.namePrefixComponent.append(displayName))
                }
            }

            if (player != null) {
                val lines = ItemProvidedHolder(reforge, itemStack).getNotMetLineComponents(player)

                if (lines.isNotEmpty()) {
                    context.lore.append(listOf(Component.empty()) + lines)
                }
            }
        }
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun revert(itemStack: ItemStack) {
        itemStack.reforge ?: return

        val fis = FastItemStack.wrap(itemStack)

        if (!plugin.configYml.getBool("reforge.display-in-name")) {
            return
        }

        if (fis.persistentDataContainer.has(tempKey, PersistentDataType.STRING)) {
            fis.setDisplayName(
                StringUtils.jsonToComponent(
                    fis.persistentDataContainer.get(
                        tempKey,
                        PersistentDataType.STRING
                    )
                )
            )

            fis.persistentDataContainer.remove(tempKey)
        }
    }
}
