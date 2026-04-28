package com.willfp.reforges.libreforge

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.reforges.api.event.ReforgeEvent

object FilterReforge : Filter<NoCompileData, Collection<String>>("reforge") {
    override fun getValue(config: Config, data: TriggerData?, key: String): Collection<String> {
        return config.getStrings(key)
    }

    override fun isMet(data: TriggerData, value: Collection<String>, compileData: NoCompileData): Boolean {
        val event = data.event as? ReforgeEvent ?: return true

        return value.any { it.equals(event.reforge.id.key, ignoreCase = true) }
    }
}
