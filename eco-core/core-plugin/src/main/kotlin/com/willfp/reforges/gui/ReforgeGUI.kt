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
import com.willfp.reforges.util.ReforgeStatus
import com.willfp.reforges.util.ReforgeUtils
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.math.pow

@Suppress("DEPRECATION")
object ReforgeGUI {
    private lateinit var menu: Menu

    @JvmStatic
    fun open(player: Player) {
        menu.open(player)
    }

    @JvmStatic
    @ConfigUpdater
    fun update(plugin: EcoPlugin) {
        val activatorSlot = slot(ItemStack(Material.ANVIL)) {
            setModifier { player, menu, _ ->
                val (status, specialCost) = ReforgeUtils.getStatus(menu.getCaptiveItems(player))

                val cost = when {
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

                val configKey = status.configKey

                Items.lookup(plugin.configYml.getString("gui.$configKey.material")).item.apply {
                    this.fast().displayName = plugin.configYml.getFormattedString("gui.$configKey.name")
                    this.fast().lore = plugin.configYml.getFormattedStrings("gui.$configKey.lore").map {
                        it.replace("%cost%", NumberUtils.format(cost))
                            .replace("%xpcost%", NumberUtils.format(xpCost.toDouble()))
                            .replace(
                                "%stone%",
                                ReforgeUtils.getReforgeStone(menu.getCaptiveItems(player)[1])?.name ?: ""
                            )
                    }
                }
            }

            onLeftClick { event, _, menu ->
                val player = event.whoClicked as Player
                val captive = menu.getCaptiveItems(player)

                val toReforge = captive.getOrNull(0) ?: return@onLeftClick
                val existingReforge = ReforgeUtils.getReforge(toReforge)
                val target = ReforgeTarget.getForItem(toReforge)

                var reforge: Reforge? = null
                var usedStone = false

                if (menu.getCaptiveItems(player).size == 2) {
                    val stone = ReforgeUtils.getReforgeStone(menu.getCaptiveItems(player)[1])
                    if (stone != null) {
                        if (stone.targets.any { it.matches(toReforge) }) {
                            reforge = stone
                            usedStone = true
                        }
                    }
                }

                if (reforge == null) {
                    val existing: MutableList<Reforge> = ArrayList()
                    if (existingReforge != null) {
                        existing.add(existingReforge)
                    }
                    reforge = ReforgeUtils.getRandomReforge(target, existing)
                }

                if (reforge == null) {
                    return@onLeftClick
                }

                var cost = 0.0

                if (EconomyManager.hasRegistrations()) {
                    cost = plugin.configYml.getDouble("reforge.cost")
                    val reforges = ReforgeUtils.getReforges(toReforge)
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
                val reforges = ReforgeUtils.getReforges(toReforge)
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
                ReforgeUtils.incrementReforges(toReforge)
                ReforgeUtils.setReforge(toReforge, reforge)
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
            setTitle(plugin.langYml.getFormattedString("menu.title"))
            setMask(FillerMask(MaskItems(*maskItems), *maskPattern))
            modfiy { builder ->
                val slot = Slot.builder(
                    ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE)
                        .setDisplayName("&r")
                        .build()
                ).apply {
                    setModifier { player, menu, _ ->
                        val status = ReforgeUtils.getStatus(
                            menu.getCaptiveItems(player)
                        ).status

                        if (status == ReforgeStatus.ALLOW || status == ReforgeStatus.ALLOW_STONE) {
                            Items.lookup(plugin.configYml.getString("gui.show-allowed.allow-material")).item
                        } else {
                            Items.lookup(plugin.configYml.getString("gui.show-allowed.deny-material")).item
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
