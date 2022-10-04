package com.willfp.reforges.gui

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.updating.ConfigUpdater
import com.willfp.eco.core.drops.DropQueue
import com.willfp.eco.core.fast.fast
import com.willfp.eco.core.gui.menu
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.slot
import com.willfp.eco.core.gui.slot.FillerMask
import com.willfp.eco.core.gui.slot.MaskItems
import com.willfp.eco.core.gui.slot.Slot
import com.willfp.eco.core.integrations.economy.EconomyManager
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.builder.ItemStackBuilder
import com.willfp.eco.util.NumberUtils
import com.willfp.reforges.reforges.PriceMultipliers
import com.willfp.reforges.reforges.Reforge
import com.willfp.reforges.reforges.ReforgeTarget
import com.willfp.reforges.reforges.ReforgeTargets
import com.willfp.reforges.reforges.util.MetadatedReforgeStatus
import com.willfp.reforges.util.ReforgeStatus
import com.willfp.reforges.util.getRandomReforge
import com.willfp.reforges.util.reforge
import com.willfp.reforges.util.reforgeStone
import com.willfp.reforges.util.timesReforged
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.math.pow

@Suppress("DEPRECATION")
object ReforgeGUI {
    private lateinit var menu: Menu

    private fun Menu.getReforgeStatus(player: Player): MetadatedReforgeStatus {
        val captive = this.getCaptiveItems(player)
        val item = captive.getOrNull(0)
        val stone = captive.getOrNull(1)

        val targets = mutableListOf<ReforgeTarget>()

        var cost = 0.0
        val status = if (item == null || item.type == Material.AIR) {
            ReforgeStatus.NO_ITEM
        } else {
            targets.addAll(ReforgeTargets.getForItem(item))
            if (targets.isEmpty()) {
                ReforgeStatus.INVALID_ITEM
            } else {
                val reforgeStone = stone.reforgeStone
                if (reforgeStone != null && reforgeStone.canBeAppliedTo(item)) {
                    cost = reforgeStone.stonePrice.toDouble()
                    ReforgeStatus.ALLOW_STONE
                } else {
                    ReforgeStatus.ALLOW
                }
            }
        }

        return MetadatedReforgeStatus(status, cost)
    }

    @JvmStatic
    fun open(player: Player) {
        menu.open(player)
    }

    @JvmStatic
    @ConfigUpdater
    fun update(plugin: EcoPlugin) {
        val activatorSlot = slot(ItemStack(Material.ANVIL)) {
            setUpdater { player, menu, _ ->
                val (status, specialCost) = menu.getReforgeStatus(player)

                val cost = when {
                    status == ReforgeStatus.ALLOW || (status == ReforgeStatus.ALLOW_STONE && specialCost < 0) -> {
                        val amountOfReforges = menu.getCaptiveItems(player)[0].timesReforged

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
                    val reforges = item.timesReforged
                    xpCost *= PriceMultipliers.getForPlayer(player).multiplier.toInt()
                    xpCost *= plugin.configYml.getDouble("reforge.cost-exponent").pow(reforges.toDouble()).toInt()
                }

                val configKey = status.configKey

                Items.lookup(plugin.configYml.getString("gui.$configKey.material")).item.apply {
                    this.fast().displayName = plugin.configYml.getFormattedString("gui.$configKey.name")
                    this.fast().lore = plugin.configYml.getFormattedStrings("gui.$configKey.lore").map {
                        it.replace("%cost%", NumberUtils.format(cost))
                            .replace("%xpcost%", NumberUtils.format(xpCost.toDouble()))
                            .replace(
                                "%stone%",
                                menu.getCaptiveItems(player).getOrNull(1).reforgeStone?.name ?: ""
                            )
                    }
                }
            }

            onLeftClick { event, _, menu ->
                val player = event.whoClicked as Player
                val captive = menu.getCaptiveItems(player)

                val item = captive.getOrNull(0) ?: return@onLeftClick
                val currentReforge = item.reforge

                val targets = ReforgeTargets.getForItem(item)


                var usedStone = false

                val stoneInMenu = menu.getCaptiveItems(player).getOrNull(1).reforgeStone

                val reforge = if (stoneInMenu != null && stoneInMenu.canBeAppliedTo(item)) {
                    usedStone = true
                    stoneInMenu
                } else {
                    val disallowed = mutableListOf<Reforge>()
                    if (currentReforge != null) {
                        disallowed.add(currentReforge)
                    }

                    targets.getRandomReforge(disallowed = disallowed)
                }

                if (reforge == null) {
                    return@onLeftClick
                }

                var cost = 0.0

                val reforges = item.timesReforged

                if (EconomyManager.hasRegistrations()) {
                    cost = plugin.configYml.getDouble("reforge.cost")
                    cost *= plugin.configYml.getDouble("reforge.cost-exponent").pow(reforges.toDouble())
                    if (reforge.requiresStone && reforge.stonePrice != -1) {
                        cost = reforge.stonePrice.toDouble()
                    }
                    cost *= PriceMultipliers.getForPlayer(player).multiplier
                    if (!EconomyManager.hasAmount(player, cost)) {
                        player.sendMessage(plugin.langYml.getMessage("insufficient-money"))
                        if (plugin.configYml.getBool("gui.insufficient-money-sound.enabled")) {
                            player.playSound(
                                player.location,
                                Sound.valueOf(
                                    plugin.configYml.getString("gui.insufficient-money-sound.id")
                                        .uppercase(Locale.getDefault())
                                ),
                                1f, plugin.configYml.getDouble("gui.insufficient-money-sound.pitch").toFloat()
                            )
                        }
                        return@onLeftClick
                    }
                }

                var xpCost = plugin.configYml.getInt("reforge.xp-cost")

                xpCost *= plugin.configYml.getDouble("reforge.cost-exponent").pow(reforges.toDouble()).toInt()
                xpCost *= PriceMultipliers.getForPlayer(player).multiplier.toInt()
                if (player.level < xpCost) {
                    player.sendMessage(plugin.langYml.getMessage("insufficient-xp"))
                    if (plugin.configYml.getBool("gui.insufficient-money-sound.enabled")) {
                        player.playSound(
                            player.location,
                            Sound.valueOf(
                                plugin.configYml.getString("gui.insufficient-money-sound.id")
                                    .uppercase(Locale.getDefault())
                            ),
                            1f, plugin.configYml.getDouble("gui.insufficient-money-sound.pitch").toFloat()
                        )
                    }
                    return@onLeftClick
                }
                if (EconomyManager.hasRegistrations()) {
                    EconomyManager.removeMoney(player, cost)
                }
                player.level = player.level - xpCost

                player.sendMessage(plugin.langYml.getMessage("applied-reforge").replace("%reforge%", reforge.name))

                item.timesReforged++
                item.reforge = reforge

                if (usedStone) {
                    val stone = menu.getCaptiveItems(player)[1]
                    stone.itemMeta = null
                    stone.amount = 0
                    if (plugin.configYml.getBool("gui.stone-sound.enabled")) {
                        player.playSound(
                            player.location,
                            Sound.valueOf(
                                plugin.configYml.getString("gui.stone-sound.id").uppercase(Locale.getDefault())
                            ),
                            1f, plugin.configYml.getDouble("gui.stone-sound.pitch").toFloat()
                        )
                    }
                }

                if (plugin.configYml.getBool("gui.sound.enabled")) {
                    player.playSound(
                        player.location,
                        Sound.valueOf(plugin.configYml.getString("gui.sound.id").uppercase(Locale.getDefault())),
                        1f, plugin.configYml.getDouble("gui.sound.pitch").toFloat()
                    )
                }
            }
        }

        val maskPattern = plugin.configYml.getStrings("gui.mask.pattern").toTypedArray()

        val maskItems = plugin.configYml.getStrings("gui.mask.materials")
            .mapNotNull { Items.lookup(it) }
            .toTypedArray()

        menu = menu(plugin.configYml.getInt("gui.rows")) {
            title = plugin.langYml.getFormattedString("menu.title")
            setMask(FillerMask(MaskItems(*maskItems), *maskPattern))
            allowChangingHeldItem()

            val slot = slot(
                ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE)
                    .setDisplayName("&r")
                    .build()
            ) {
                setUpdater { player, menu, _ ->
                    val status = menu.getReforgeStatus(player).status

                    if (status == ReforgeStatus.ALLOW || status == ReforgeStatus.ALLOW_STONE) {
                        Items.lookup(plugin.configYml.getString("gui.show-allowed.allow-material")).item
                    } else {
                        Items.lookup(plugin.configYml.getString("gui.show-allowed.deny-material")).item
                    }
                }
            }

            val allowedPattern = plugin.configYml.getStrings("gui.show-allowed.pattern")
            for (i in 1..allowedPattern.size) {
                val row = allowedPattern[i - 1]
                for (j in 1..9) {
                    if (row[j - 1] != '0') {
                        setSlot(i, j, slot)
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
                slot(
                    ItemStackBuilder(Items.lookup(plugin.configYml.getString("gui.close.material")))
                        .setDisplayName(plugin.langYml.getFormattedString("menu.close"))
                        .build()
                ) {
                    onLeftClick { event, _, _ -> event.whoClicked.closeInventory() }
                }
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
