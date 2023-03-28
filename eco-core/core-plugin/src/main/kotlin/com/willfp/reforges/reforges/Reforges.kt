package com.willfp.reforges.reforges

import com.google.common.collect.ImmutableSet
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.registry.Registry
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.loader.configs.ConfigCategory
import com.willfp.libreforge.loader.configs.LegacyLocation
import com.willfp.reforges.ReforgesPlugin

@Suppress("UNUSED")
object Reforges : ConfigCategory("reforge", "reforges") {
    private val registry = Registry<Reforge>()

    override val legacyLocation = LegacyLocation(
        "reforges.yml",
        "reforges"
    )

    /**
     * Get all registered [Reforge]s.
     *
     * @return A list of all [Reforge]s.
     */
    @JvmStatic
    fun values(): Set<Reforge> {
        return ImmutableSet.copyOf(registry.values())
    }

    /**
     * Get [Reforge] matching key.
     *
     * @param key The key to search for.
     * @return The matching [Reforge], or null if not found.
     */
    @JvmStatic
    fun getByKey(key: String?): Reforge? {
        return if (key == null) {
            null
        } else registry[key]
    }

    override fun clear(plugin: LibreforgePlugin) {
        registry.clear()
    }

    override fun acceptConfig(plugin: LibreforgePlugin, id: String, config: Config) {
        registry.register(Reforge(id, config, plugin as ReforgesPlugin))
    }
}
