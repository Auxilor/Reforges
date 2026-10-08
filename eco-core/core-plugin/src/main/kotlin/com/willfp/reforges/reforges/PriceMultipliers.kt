package com.willfp.reforges.reforges

import com.willfp.reforges.plugin
import org.bukkit.entity.Player

@Suppress("UNUSED")
object PriceMultipliers {
    @Volatile
    private var registry = emptyList<PriceMultiplier>()

    private val NO_MULTIPLIER = PriceMultiplier("none", 1.0, 0)

    fun getForPlayer(player: Player): PriceMultiplier {
        var current = NO_MULTIPLIER

        for (multiplier in registry) {
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
        return registry.toList()
    }

    internal fun update() {
        registry = plugin.configYml.getSubsections("price-multipliers").map { config ->
            PriceMultiplier(
                config.getString("permission"),
                config.getDouble("multiplier"),
                config.getInt("priority")
            )
        }
    }
}
