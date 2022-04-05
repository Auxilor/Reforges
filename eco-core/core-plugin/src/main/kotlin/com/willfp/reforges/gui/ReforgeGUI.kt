package com.willfp.reforges.gui

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.updating.ConfigUpdater
import com.willfp.eco.core.drops.DropQueue
import com.willfp.eco.core.gui.menu
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.slot
import com.willfp.eco.core.gui.slot.FillerMask
import com.willfp.eco.core.gui.slot.MaskItems
import com.willfp.eco.core.gui.slot.Slot
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.builder.ItemStackBuilder
import com.willfp.eco.util.NumberUtils
import com.willfp.reforges.ReforgesPlugin
import com.willfp.reforges.reforges.PriceMultipliers
import com.willfp.reforges.reforges.util.ReforgeHandler
import com.willfp.reforges.reforges.util.ReforgeStatus
import com.willfp.reforges.reforges.util.ReforgeUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.math.pow

@Suppress("DEPRECATION")
object ReforgeGUI {
    @JvmStatic
    lateinit var menu: Menu

    init {
        update(ReforgesPlugin.getInstance())
    }

    @JvmStatic
    @ConfigUpdater
    fun update(plugin: EcoPlugin) {
        val handler = ReforgeHandler(plugin)

        val activatorSlot = slot(ItemStack(Material.ANVIL)) {
            setModifier { player, menu, previous ->
                val meta = previous.itemMeta ?: return@setModifier

                val (status, specialCost) = ReforgeUtils.getStatus(menu.getCaptiveItems(player))

                val cost: Double = when {
                    status == ReforgeStatus.ALLOW || (status == ReforgeStatus.ALLOW_STONE && specialCost < 0) -> {
                        val amountOfReforges = ReforgeUtils.getReforges(menu.getCaptiveItems(player)[0])
                        plugin.configYml.getDouble("reforge.cost") *
                                plugin.configYml.getDouble("reforge.cost-exponent").pow(amountOfReforges) *
                                PriceMultipliers.getForPlayer(player).multiplier
                    }
                    status == ReforgeStatus.ALLOW_STONE -> {
                        specialCost
                    }
                    else -> 0.0 // Never used, but at least kotlin can shut up
                }

                var xpCost = plugin.configYml.getInt("reforge.xp-cost")
                if (status == ReforgeStatus.ALLOW) {
                    val item = menu.getCaptiveItems(player)[0]
                    val reforges = ReforgeUtils.getReforges(item)
                    xpCost *= PriceMultipliers.getForPlayer(player).multiplier.toInt()
                    xpCost *= plugin.configYml.getDouble("reforge.cost-exponent").pow(reforges.toDouble()).toInt()
                }

                when (status) {
                    ReforgeStatus.INVALID_ITEM -> {
                        previous.type = Material.getMaterial(
                            plugin.configYml.getString("gui.invalid-item.material").uppercase()
                        )!!
                        meta.setDisplayName(plugin.configYml.getFormattedString("gui.invalid-item.name"))
                        meta.lore = plugin.configYml.getFormattedStrings("gui.invalid-item.lore").map {
                            it.replace("%cost%", NumberUtils.format(cost))
                                .replace("%xpcost%", NumberUtils.format(xpCost.toDouble()))
                        }
                    }
                    ReforgeStatus.ALLOW -> {
                        previous.type = Material.getMaterial(
                            plugin.configYml.getString("gui.allow.material").uppercase()
                        )!!
                        meta.setDisplayName(plugin.configYml.getFormattedString("gui.allow.name"))
                        meta.lore = plugin.configYml.getFormattedStrings("gui.allow.lore").map {
                            it.replace("%cost%", NumberUtils.format(cost))
                                .replace("%xpcost%", NumberUtils.format(xpCost.toDouble()))
                        }
                    }
                    ReforgeStatus.ALLOW_STONE -> {
                        previous.type = Material.getMaterial(
                            plugin.configYml.getString("gui.allow-stone.material").uppercase()
                        )!!
                        meta.setDisplayName(plugin.configYml.getFormattedString("gui.allow-stone.name"))
                        meta.lore = plugin.configYml.getFormattedStrings("gui.allow-stone.lore").map {
                            it.replace("%cost%", NumberUtils.format(cost))
                                .replace("%xpcost%", NumberUtils.format(xpCost.toDouble()))
                                .replace("%stone%", ReforgeUtils.getReforgeStone(menu.getCaptiveItems(player)[1]).name)
                        }
                    }
                    ReforgeStatus.NO_ITEM -> {
                        previous.type = Material.getMaterial(
                            plugin.configYml.getString("gui.no-item.material").uppercase()
                        )!!
                        meta.setDisplayName(plugin.configYml.getFormattedString("gui.no-item.name"))
                        meta.lore = plugin.configYml.getFormattedStrings("gui.no-item.lore").map {
                            it.replace("%cost%", NumberUtils.format(cost))
                                .replace("%xpcost%", NumberUtils.format(xpCost.toDouble()))
                        }
                    }
                }

                previous.itemMeta = meta
            }
            onLeftClick(handler::handleReforgeClick)
        }

        val maskPattern = plugin.configYml.getStrings("gui.mask.pattern").toTypedArray()

        val maskItems = plugin.configYml.getStrings("gui.mask.materials")
            .mapNotNull { Items.lookup(it) }
            .toTypedArray()

        val allowItem = Items.lookup(plugin.configYml.getString("gui.show-allowed.allow-material")).item
        val denyItem = Items.lookup(plugin.configYml.getString("gui.show-allowed.deny-material")).item
        val closeItem = Items.lookup(plugin.configYml.getString("gui.close.material")).item

        menu = menu(plugin.configYml.getInt("gui.rows")) {
            setTitle(plugin.langYml.getFormattedString("menu.title"))
            setMask(FillerMask(MaskItems(*maskItems), *maskPattern))
            modfiy { builder ->
                val slot = Slot.builder(
                    ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE)
                        .setDisplayName("&r")
                        .build()
                ).apply {
                    setModifier { player, menu, previous ->
                        val status = ReforgeUtils.getStatus(
                            menu.getCaptiveItems(player)
                        ).status

                        if (status == ReforgeStatus.ALLOW || status == ReforgeStatus.ALLOW_STONE) {
                            previous.type = allowItem.type
                            previous.itemMeta = allowItem.itemMeta
                        } else {
                            previous.type = denyItem.type
                            previous.itemMeta = denyItem.itemMeta
                        }
                    }
                }.build()

                val allowedPattern = plugin.configYml.getStrings("gui.show-allowed.pattern")
                for (i in 1..allowedPattern.size) {
                    val row = allowedPattern[i - 1]
                    for (j in 1..9) {
                        if (row[j - 1] != '0') {
                            builder.setSlot(i, j, slot)
                        }
                    }
                }
            }
            setSlot(
                plugin.configYml.getInt("gui.item-slot.row"),
                plugin.configYml.getInt("gui.item-slot.column"),
                Slot.builder().setCaptive().build()
            )
            setSlot(
                plugin.configYml.getInt("gui.stone-slot.row"),
                plugin.configYml.getInt("gui.stone-slot.column"),
                Slot.builder().setCaptive().build()
            )
            setSlot(
                plugin.configYml.getInt("gui.activator-slot.row"),
                plugin.configYml.getInt("gui.activator-slot.column"),
                activatorSlot
            )
            setSlot(
                plugin.configYml.getInt("gui.close.location.row"),
                plugin.configYml.getInt("gui.close.location.column"),
                Slot.builder(
                    ItemStackBuilder(closeItem)
                        .setDisplayName(plugin.langYml.getFormattedString("menu.close"))
                        .build()
                ).onLeftClick { event, _, _ ->
                    event.whoClicked.closeInventory()
                }.build()
            )
            onClose { event, menu ->
                DropQueue(event.player as Player)
                    .addItems(menu.getCaptiveItems(event.player as Player))
                    .setLocation(event.player.eyeLocation)
                    .forceTelekinesis()
                    .push()
            }
        }
    }
}