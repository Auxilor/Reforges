package com.willfp.reforges.config

import wiki.ecohub.config.ConfigType
import wiki.ecohub.config.file.StaticBaseConfig
import wiki.ecohub.core.plugin.EcoPlugin
import wiki.ecohub.game.item.Items
import wiki.ecohub.game.item.lookup.TestableItem
import wiki.ecohub.rules.holder.slot.SlotType
import wiki.ecohub.rules.holder.slot.SlotTypeAny
import wiki.ecohub.rules.holder.slot.SlotTypes
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
