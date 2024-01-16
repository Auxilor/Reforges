package com.willfp.reforges.config

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.ConfigType
import com.willfp.eco.core.config.StaticBaseConfig
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.TestableItem
import com.willfp.libreforge.slot.SlotType
import com.willfp.libreforge.slot.SlotTypes
import com.willfp.libreforge.slot.impl.SlotTypeAny
import java.util.Locale
import java.util.function.Consumer

class TargetYml(plugin: EcoPlugin) : StaticBaseConfig("target", plugin, ConfigType.YAML) {
    /**
     * Get all target names.
     *
     * @return Set of all names.
     */
    val targets: List<String>
        get() = getKeys(false)

    /**
     * Get all materials from a target name.
     *
     * @param target The name of the target.
     * @return All materials.
     */
    fun getTargetItems(target: String): Set<TestableItem> {
        val items: MutableSet<TestableItem> = HashSet()
        this.getStrings("$target.items")
            .forEach(Consumer { s: String -> items.add(Items.lookup(s.uppercase(Locale.getDefault()))) })
        return items
    }

    /**
     * Get a [SlotType] for the target.
     *
     * @param target The name of the target.
     * @return The [SlotType].
     */
    fun getSlot(target: String): SlotType {
        return SlotTypes[this.getString("$target.slot")] ?: SlotTypeAny
    }
}
