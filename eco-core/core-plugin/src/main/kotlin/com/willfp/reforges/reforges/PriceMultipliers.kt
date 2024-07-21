package com.willfp.reforges.reforges

import com.willfp.eco.core.EcoPlugin
import org.bukkit.entity.Player

@Suppress("UNUSED")
object PriceMultipliers {
    private val REGISTRY = mutableListOf<PriceMultiplier>()
    private val NO_MULTIPLIER = PriceMultiplier("none", 1.0, 0)

    fun getForPlayer(player: Player): PriceMultiplier {
        var current = NO_MULTIPLIER

        for (multiplier in REGISTRY) {
            if (multiplier.priority < current.priority) {
                continue
            }

            if (!player.hasPermission(multiplier.permission)) {
                continue
            }

            current = multiplier
        }

        return current
    }

    /** The price multiplier from permissions. */
    val Player.reforgePriceMultiplier: Double
        get() = getForPlayer(this).multiplier

    fun values(): List<PriceMultiplier> {
        return REGISTRY.toList()
    }

    internal fun update(plugin: EcoPlugin) {
        REGISTRY.clear()

        for (config in plugin.configYml.getSubsections("price-multipliers")) {
            val multiplier = PriceMultiplier(
                config.getString("permission"),
                config.getDouble("multiplier"),
                config.getInt("priority")
            )
            REGISTRY.add(multiplier)
        }
    }
}
