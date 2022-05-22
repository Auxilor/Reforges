package com.willfp.reforges.util

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.integrations.economy.EconomyManager
import com.willfp.reforges.reforges.PriceMultipliers.getForPlayer
import com.willfp.reforges.reforges.Reforge
import com.willfp.reforges.reforges.ReforgeTarget.Companion.getForItem
import com.willfp.reforges.util.ReforgeUtils.getRandomReforge
import com.willfp.reforges.util.ReforgeUtils.getReforge
import com.willfp.reforges.util.ReforgeUtils.getReforgeStone
import com.willfp.reforges.util.ReforgeUtils.getReforges
import com.willfp.reforges.util.ReforgeUtils.incrementReforges
import com.willfp.reforges.util.ReforgeUtils.setReforge
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import java.util.*
import kotlin.math.pow

class ReforgeHandler(private val plugin: EcoPlugin) {
    fun handleReforgeClick(
        event: InventoryClickEvent,
        menu: Menu
    ) {
        val player = event.whoClicked as Player
        val toReforge =
            (if (menu.getCaptiveItems(player).isEmpty()) null else menu.getCaptiveItems(player)[0]) ?: return
        val existingReforge = getReforge(toReforge)
        val target = getForItem(toReforge)
        var reforge: Reforge? = null
        var usedStone = false
        if (menu.getCaptiveItems(player).size == 2) {
            val stone = getReforgeStone(menu.getCaptiveItems(player)[1])
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
            reforge = getRandomReforge(target, existing)
        }

        if (reforge == null) {
            return
        }

        var cost = 0.0

        if (EconomyManager.hasRegistrations()) {
            cost = plugin.configYml.getDouble("reforge.cost")
            val reforges = getReforges(toReforge)
            cost *= plugin.configYml.getDouble("reforge.cost-exponent").pow(reforges.toDouble())
            if (reforge.requiresStone && reforge.stonePrice != -1) {
                cost = reforge.stonePrice.toDouble()
            }
            cost *= getForPlayer(player).multiplier
            if (!EconomyManager.hasAmount(player, cost)) {
                player.sendMessage(plugin.langYml.getMessage("insufficient-money"))
                if (plugin.configYml.getBool("gui.insufficient-money-sound.enabled")) {
                    player.playSound(
                        player.location,
                        Sound.valueOf(
                            plugin.configYml.getString("gui.insufficient-money-sound.id").uppercase(Locale.getDefault())
                        ),
                        1f, plugin.configYml.getDouble("gui.insufficient-money-sound.pitch").toFloat()
                    )
                }
                return
            }
        }
        var xpCost = plugin.configYml.getInt("reforge.xp-cost")
        val reforges = getReforges(toReforge)
        xpCost *= plugin.configYml.getDouble("reforge.cost-exponent").pow(reforges.toDouble()).toInt()
        xpCost *= getForPlayer(player).multiplier.toInt()
        if (player.level < xpCost) {
            player.sendMessage(plugin.langYml.getMessage("insufficient-xp"))
            if (plugin.configYml.getBool("gui.insufficient-money-sound.enabled")) {
                player.playSound(
                    player.location,
                    Sound.valueOf(
                        plugin.configYml.getString("gui.insufficient-money-sound.id").uppercase(Locale.getDefault())
                    ),
                    1f, plugin.configYml.getDouble("gui.insufficient-money-sound.pitch").toFloat()
                )
            }
            return
        }
        if (EconomyManager.hasRegistrations()) {
            EconomyManager.removeMoney(player, cost)
        }
        player.level = player.level - xpCost
        player.sendMessage(plugin.langYml.getMessage("applied-reforge").replace("%reforge%", reforge.name))
        incrementReforges(toReforge)
        setReforge(toReforge, reforge)
        if (usedStone) {
            val stone = menu.getCaptiveItems(player)[1]
            stone.itemMeta = null
            stone.amount = 0
            if (plugin.configYml.getBool("gui.stone-sound.enabled")) {
                player.playSound(
                    player.location,
                    Sound.valueOf(plugin.configYml.getString("gui.stone-sound.id").uppercase(Locale.getDefault())),
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
