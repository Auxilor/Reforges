package com.willfp.reforges.reforges

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.updating.ConfigUpdater
import org.bukkit.entity.Player

@Suppress("UNUSED")
object PriceMultipliers {
    private val REGISTRY = mutableListOf<PriceMultiplier>()
    private val NO_MULTIPLIER = PriceMultiplier("none", 1.0, 0)

    /**
     * Get the permission multiplier for a given player.
     *
     * @param player The player.
     * @return The multiplier.
     */
    @JvmStatic
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

    /**
     * List of all registered multipliers.
     *
     * @return The multipliers.
     */
    @JvmStatic
    fun values(): List<PriceMultiplier> {
        return REGISTRY.toList()
    }

    @ConfigUpdater
    @JvmStatic
    fun update(plugin: EcoPlugin) {
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
