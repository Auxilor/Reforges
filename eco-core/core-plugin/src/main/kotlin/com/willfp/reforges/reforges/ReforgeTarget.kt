package com.willfp.reforges.reforges

import com.google.common.collect.ImmutableSet
import com.willfp.eco.core.config.updating.ConfigUpdater
import com.willfp.eco.core.items.TestableItem
import com.willfp.eco.core.recipe.parts.EmptyTestableItem
import com.willfp.reforges.ReforgesPlugin
import com.willfp.reforges.ReforgesPlugin.Companion.instance
import org.bukkit.inventory.ItemStack

class ReforgeTarget(
    val name: String,
    val slot: Slot,
    val items: MutableSet<TestableItem>
) {
    init {
        items.removeIf { it is EmptyTestableItem }
    }

    fun matches(itemStack: ItemStack): Boolean {
        for (item in items) {
            if (item.matches(itemStack)) {
                return true
            }
        }
        return false
    }

    enum class Slot {
        HANDS,
        ARMOR,
        ANY
    }

    companion object {
        private val registered = mutableMapOf<String, ReforgeTarget>()

        val ALL = ReforgeTarget("all", Slot.ANY, HashSet())

        init {
            registered["all"] = ALL
            update(instance)
        }

        /**
         * Get ReforgeTarget matching name.
         *
         * @param name The name to search for.
         * @return The matching ReforgeTarget, or null if not found.
         */
        @JvmStatic
        fun getByName(name: String): ReforgeTarget? {
            return registered[name]
        }

        /**
         * Get target from item.
         *
         * @param item The item.
         * @return The target.
         */
        @JvmStatic
        fun getForItem(item: ItemStack): List<ReforgeTarget> {
            return registered.values
                .filter { !it.name.equals("all", ignoreCase = true) }
                .filter { it.matches(item) }
        }

        /**
         * Update all targets.
         *
         * @param plugin Instance of Reforges.
         */
        @ConfigUpdater
        @JvmStatic
        fun update(plugin: ReforgesPlugin) {
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

        /**
         * Get all targets.
         *
         * @return A set of all targets.
         */
        @JvmStatic
        fun values(): Set<ReforgeTarget> {
            return ImmutableSet.copyOf(registered.values)
        }
    }
}
