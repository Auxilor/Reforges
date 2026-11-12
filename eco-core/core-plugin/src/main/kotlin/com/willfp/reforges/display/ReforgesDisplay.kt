package com.willfp.reforges.display

import com.willfp.reforges.plugin
import com.willfp.reforges.reforges.ReforgeTargets
import com.willfp.reforges.util.reforge
import com.willfp.reforges.util.reforgeStone
import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import wiki.ecohub.game.item.FastItemStack
import wiki.ecohub.game.item.builder.SkullUtils
import wiki.ecohub.game.item.fast
import wiki.ecohub.rules.holder.ItemProvidedHolder
import wiki.ecohub.ui.display.DisplayContext
import wiki.ecohub.ui.display.DisplayModule
import wiki.ecohub.ui.display.DisplayPriority
import wiki.ecohub.ui.text.StringUtils

@Suppress("DEPRECATION")
object ReforgesDisplay : DisplayModule(plugin, DisplayPriority.HIGH) {
    private val tempKey = plugin.namespacedKeyFactory.create("temp")

    override fun display(context: DisplayContext) {
        val itemStack = context.getItemStack()
        val player = context.getPlayer()
        val targets = ReforgeTargets.getForItem(itemStack)
        val fast = itemStack.fast()

        val stone = fast.persistentDataContainer.reforgeStone

        if (targets.isEmpty() && stone == null) {
            return
        }

        val reforge = fast.persistentDataContainer.reforge

        if (reforge == null && stone == null && plugin.configYml.getBool("reforge.show-reforgable")) {
            if (context.getProperties().inGui) {
                return
            }

            context.getLore().append(
                plugin.configYml.getStrings("reforge.reforgable-suffix").map { StringUtils.formatToRichComponent(it) }
            )
        }

        if (stone != null) {
            val meta = itemStack.itemMeta
            meta.setDisplayName(stone.config.getFormattedString("stone.name").replace("%reforge%", stone.name))
            val stoneMeta = stone.stone.itemMeta
            if (stoneMeta is SkullMeta) {
                val stoneTexture = SkullUtils.getSkullTexture(stoneMeta)

                if (stoneTexture != null && meta is SkullMeta) {
                    SkullUtils.setSkullTexture(meta, stoneTexture)
                }
            }
            itemStack.itemMeta = meta

            context.getLore().prepend(
                stone.config.getStrings("stone.lore")
                    .map { it.replace("%price%", if (player == null) "" else stone.stonePrice?.getDisplay(player) ?: "") }
                    .map { StringUtils.formatToRichComponent(it) }
            )
        }

        if (reforge != null) {
            if (plugin.configYml.getBool("reforge.display-in-lore")) {
                context.getLore().append(
                    plugin.configYml.getStrings("reforge.reforged-prefix")
                        .map { it.replace("%reforge%", reforge.name) }
                        .map { StringUtils.formatToRichComponent(it) } + reforge.description.map { StringUtils.formatToRichComponent(it, context.getPlaceholderContext()) }
                )
            }

            if (plugin.configYml.getBool("reforge.display-in-name")) {
                val displayName = fast.getDisplayNameComponent()

                if (!fast.getDisplayName().contains(reforge.name)) {
                    fast.persistentDataContainer.set(
                        tempKey,
                        PersistentDataType.STRING,
                        StringUtils.componentToJson(displayName)
                    )

                    fast.setDisplayName(reforge.namePrefixComponent.append(displayName))
                }
            }

            if (player != null) {
                val lines = ItemProvidedHolder(reforge, itemStack).getNotMetLineComponents(player)

                if (lines.isNotEmpty()) {
                    context.getLore().append(listOf(Component.empty()) + lines)
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
