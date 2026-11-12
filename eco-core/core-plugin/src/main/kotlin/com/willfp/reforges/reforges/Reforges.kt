package com.willfp.reforges.reforges

import com.google.common.collect.ImmutableSet
import wiki.ecohub.config.Config
import wiki.ecohub.config.LegacyLocation
import wiki.ecohub.config.category.NativeConfigCategory
import wiki.ecohub.core.plugin.EcoPlugin
import wiki.ecohub.core.registry.Registry
import wiki.ecohub.modules.recipe.Recipes

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
        for (reforge in registry.values()) {
            Recipes.unregister(plugin.namespacedKeyFactory.create("stone_" + reforge.id.key))
        }
        registry.clear()
    }

    override fun acceptConfig(plugin: LibreforgePlugin, id: String, config: Config) {
        registry.register(Reforge(id, config))
    }
}
