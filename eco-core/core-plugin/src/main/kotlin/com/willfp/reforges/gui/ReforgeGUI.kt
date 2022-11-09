package com.willfp.reforges.gui

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.emptyConfig
import com.willfp.eco.core.config.updating.ConfigUpdater
import com.willfp.eco.core.drops.DropQueue
import com.willfp.eco.core.gui.captiveSlot
import com.willfp.eco.core.gui.menu
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.menu.MenuEvent
import com.willfp.eco.core.gui.menu.events.CaptiveItemChangeEvent
import com.willfp.eco.core.gui.onEvent
import com.willfp.eco.core.gui.slot
import com.willfp.eco.core.gui.slot.CustomSlot
import com.willfp.eco.core.gui.slot.FillerMask
import com.willfp.eco.core.gui.slot.MaskItems
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.builder.ItemStackBuilder
import com.willfp.eco.core.items.builder.modify
import com.willfp.eco.core.items.isEmpty
import com.willfp.eco.core.price.ConfiguredPrice
import com.willfp.eco.core.sound.PlayableSound
import com.willfp.ecomponent.CaptiveItem
import com.willfp.ecomponent.menuStateVar
import com.willfp.ecomponent.setSlot
import com.willfp.reforges.reforges.PriceMultipliers
import com.willfp.reforges.reforges.PriceMultipliers.reforgePriceMultiplier
import com.willfp.reforges.reforges.Reforge
import com.willfp.reforges.reforges.ReforgeTarget
import com.willfp.reforges.reforges.ReforgeTargets
import com.willfp.reforges.util.ReforgeStatus
import com.willfp.reforges.util.getRandomReforge
import com.willfp.reforges.util.reforge
import com.willfp.reforges.util.reforgeStone
import com.willfp.reforges.util.timesReforged
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.Locale
import kotlin.math.pow


private data class ReforgeGUIStatus(
    val status: ReforgeStatus,
    val price: ConfiguredPrice,
    val isStonePrice: Boolean
)

private class ReforgePriceChangeEvent : MenuEvent

private val Menu.reforgeStatus by menuStateVar(
    ReforgeGUIStatus(
        ReforgeStatus.NO_ITEM,
        ConfiguredPrice.createOrFree(emptyConfig()),
        false
    )
)

private class IndicatorSlot(
    plugin: EcoPlugin
) : CustomSlot() {
    private val slot = slot { player, menu ->
        val status = menu.reforgeStatus[player].status

        if (status == ReforgeStatus.ALLOW || status == ReforgeStatus.ALLOW_STONE) {
            Items.lookup(plugin.configYml.getString("gui.show-allowed.allow-material")).item
        } else {
            Items.lookup(plugin.configYml.getString("gui.show-allowed.deny-material")).item
        }
    }

    init {
        init(slot)
    }
}

private class ActivatorSlot(
    plugin: EcoPlugin,
    itemToReforge: CaptiveItem,
    reforgeStone: CaptiveItem
) : CustomSlot() {
    private val slot = slot({ player, menu ->
        val (status, price) = menu.reforgeStatus[player]

        val configKey = status.configKey

        Items.lookup(plugin.configYml.getString("gui.$configKey.material")).modify {
            setDisplayName(plugin.configYml.getString("gui.$configKey.name"))
            addLoreLines(plugin.configYml.getStrings("gui.$configKey.lore").map {
                it.replace("%price%", price.getDisplay(player))
                    .replace(
                        "%stone%",
                        reforgeStone[player]?.reforgeStone?.name ?: ""
                    )
                    // Legacy
                    .replace("%cost%", price.getDisplay(player))
                    .replace("%xpcost%", price.getDisplay(player))
            })
        }
    }) {
        onLeftClick { event, _, menu ->
            val player = event.whoClicked as Player

            val item = itemToReforge[player] ?: return@onLeftClick
            val currentReforge = item.reforge

            val targets = ReforgeTargets.getForItem(item)

            var usedStone = false

            val stoneInMenu = reforgeStone[player]?.reforgeStone

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

            val price = menu.reforgeStatus[player].price

            if (!price.canAfford(player)) {
                player.sendMessage(
                    plugin.langYml.getMessage("cannot-afford-price").replace("%price%", price.getDisplay(player))
                )

                if (plugin.configYml.getBool("gui.cannot-afford-sound.enabled")) {
                    PlayableSound.create(
                        plugin.configYml.getSubsection("gui.cannot-afford-sound")
                    )?.playTo(player)
                }
                return@onLeftClick
            }

            price.pay(player)

            player.sendMessage(plugin.langYml.getMessage("applied-reforge").replace("%reforge%", reforge.name))

            item.timesReforged++
            item.reforge = reforge

            if (usedStone) {
                val stone = reforgeStone[player]
                stone?.itemMeta = null
                stone?.amount = 0
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

            menu.callEvent(player, ReforgePriceChangeEvent())
        }
    }

    init {
        init(slot)
    }
}

@Suppress("DEPRECATION")
object ReforgeGUI {
    private lateinit var menu: Menu

    private lateinit var itemToReforge: CaptiveItem
    private lateinit var reforgeStone: CaptiveItem

    private lateinit var defaultPrice: ConfiguredPrice

    @JvmStatic
    fun open(player: Player) {
        menu.open(player)
    }

    @JvmStatic
    @ConfigUpdater
    fun update(plugin: EcoPlugin) {
        itemToReforge = CaptiveItem()
        reforgeStone = CaptiveItem()

        defaultPrice = ConfiguredPrice.createOrFree(plugin.configYml.getSubsection("reforge.price"))

        val maskPattern = plugin.configYml.getStrings("gui.mask.pattern").toTypedArray()

        val maskItems = plugin.configYml.getStrings("gui.mask.materials")
            .mapNotNull { Items.lookup(it) }
            .toTypedArray()

        menu = menu(plugin.configYml.getInt("gui.rows")) {
            title = plugin.langYml.getFormattedString("menu.title")
            setMask(FillerMask(MaskItems(*maskItems), *maskPattern))

            allowChangingHeldItem()

            val allowedPattern = plugin.configYml.getStrings("gui.show-allowed.pattern")
            for (i in 1..allowedPattern.size) {
                val row = allowedPattern[i - 1]
                for (j in 1..9) {
                    if (row[j - 1] != '0') {
                        setSlot(i, j, IndicatorSlot(plugin))
                    }
                }
            }

            setSlot(
                plugin.configYml.getInt("gui.item-slot.row"),
                plugin.configYml.getInt("gui.item-slot.column"),
                captiveSlot(),
                bindCaptive = itemToReforge
            )

            setSlot(
                plugin.configYml.getInt("gui.stone-slot.row"),
                plugin.configYml.getInt("gui.stone-slot.column"),
                captiveSlot(),
                bindCaptive = reforgeStone
            )

            setSlot(
                plugin.configYml.getInt("gui.activator-slot.row"),
                plugin.configYml.getInt("gui.activator-slot.column"),
                ActivatorSlot(plugin, itemToReforge, reforgeStone)
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

            onEvent<ReforgePriceChangeEvent> { player, menu, _ ->
                val status = menu.reforgeStatus[player]

                val item = itemToReforge[player]

                val reforges = item?.timesReforged ?: 0

                var multiplier = if (status.isStonePrice) 1.0 else {
                    plugin.configYml.getDouble("reforge.cost-exponent")
                        .pow(reforges.toDouble())
                }

                multiplier *= player.reforgePriceMultiplier

                status.price.setMultiplier(player, multiplier)
            }

            onEvent<CaptiveItemChangeEvent> { player, menu, _ ->
                val item = itemToReforge[player]
                val stone = reforgeStone[player]

                val targets = mutableListOf<ReforgeTarget>()

                var price = defaultPrice

                var isStonePrice = false

                val status = if (item.isEmpty) {
                    ReforgeStatus.NO_ITEM
                } else {
                    targets.addAll(ReforgeTargets.getForItem(item))
                    if (targets.isEmpty()) {
                        ReforgeStatus.INVALID_ITEM
                    } else {
                        val reforgeStone = stone.reforgeStone
                        if (reforgeStone == null) {
                            ReforgeStatus.ALLOW
                        } else {
                            if (reforgeStone.canBeAppliedTo(item)) {
                                price = reforgeStone.stonePrice ?: defaultPrice
                                isStonePrice = true

                                ReforgeStatus.ALLOW_STONE
                            } else {
                                ReforgeStatus.INVALID_ITEM
                            }
                        }
                    }
                }

                menu.reforgeStatus[player] = ReforgeGUIStatus(status, price, isStonePrice)
                menu.callEvent(player, ReforgePriceChangeEvent())
            }

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
