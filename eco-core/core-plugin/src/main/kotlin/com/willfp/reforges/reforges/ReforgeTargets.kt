package com.willfp.reforges.reforges

import com.google.common.collect.ImmutableSet
import com.willfp.libreforge.slot.impl.SlotTypeAny
import com.willfp.reforges.ReforgesPlugin
import org.bukkit.inventory.ItemStack

object ReforgeTargets {
    private val registered = mutableMapOf<String, ReforgeTarget>()

    val ALL = ReforgeTarget("all", SlotTypeAny, HashSet())

    init {
        registered["all"] = ALL
        update(ReforgesPlugin.instance)
    }

    fun getByName(name: String): ReforgeTarget? {
        return registered[name]
    }

    fun getForItem(item: ItemStack?): List<ReforgeTarget> {
        if (item == null) {
            return emptyList()
        }

        return registered.values
            .filter { !it.id.equals("all", ignoreCase = true) }
            .filter { it.matches(item) }
    }

    internal fun update(plugin: ReforgesPlugin) {
        ALL.items.clear()
        for (id in ArrayList(registered.keys)) {
            if (id.equals("all", ignoreCase = true)) {
                continue
            }
            registered.remove(id)
        }
        for (id in plugin.targetYml.targets) {
            val target = ReforgeTarget(
                id,
                plugin.targetYml.getSlot(id),
                plugin.targetYml.getTargetItems(id).toMutableSet()
            )
            registered[id] = target
            ALL.items.addAll(target.items)
        }
    }

    fun values(): Set<ReforgeTarget> {
        return ImmutableSet.copyOf(registered.values)
    }
}