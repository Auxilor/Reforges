package com.willfp.reforges.effects

import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.reforges.ReforgesPlugin
import com.willfp.reforges.reforges.util.Watcher
import org.bukkit.event.Listener
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

abstract class Effect(
    val id: String
) : Listener, Watcher {
    protected val plugin: ReforgesPlugin = ReforgesPlugin.getInstance()

    val uuid: UUID = UUID.nameUUIDFromBytes(id.encodeToByteArray())

    init {
        register()
    }

    private fun register() {
        Effects.addNewEffect(this)
    }

    /**
     * Handle reforge application.
     *
     * @param meta   The meta.
     * @param config The config.
     */
    open fun handleApplication(
        meta: ItemMeta,
        config: JSONConfig
    ) {
        // Override when needed
    }

    /**
     * Handle reforge removal.
     *
     * @param meta The meta.
     */
    open fun handleRemoval(meta: ItemMeta) {
        // Override when needed
    }
}