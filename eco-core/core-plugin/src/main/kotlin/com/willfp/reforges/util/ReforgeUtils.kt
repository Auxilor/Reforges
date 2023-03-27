package com.willfp.reforges.util

import com.willfp.eco.core.fast.fast
import com.willfp.reforges.ReforgesPlugin
import com.willfp.reforges.reforges.Reforge
import com.willfp.reforges.reforges.ReforgeTarget
import com.willfp.reforges.reforges.Reforges
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

private val plugin = ReforgesPlugin.instance
private val reforgeKey = plugin.namespacedKeyFactory.create("reforge")
private val reforgeAmountKey = plugin.namespacedKeyFactory.create("reforge_amount")
private val reforgeStoneKey = plugin.namespacedKeyFactory.create("reforge_stone")

var ItemStack?.reforge: Reforge?
    get() {
        this ?: return null
        return this.fast().persistentDataContainer.reforge
    }
    set(value) {
        this ?: return
        this.fast().persistentDataContainer.reforge = value
    }

var ItemMeta?.reforge: Reforge?
    get() {
        this ?: return null
        return this.persistentDataContainer.reforge
    }
    set(value) {
        this ?: return
        this.persistentDataContainer.reforge = value
    }

var PersistentDataContainer?.reforge: Reforge?
    get() {
        this ?: return null

        if (!this.has(reforgeKey, PersistentDataType.STRING)) {
            return null
        }

        val active = this.get(reforgeKey, PersistentDataType.STRING)
        return Reforges.getByKey(active)
    }
    set(value) {
        this ?: return
        if (value == null) {
            this.remove(reforgeKey)
        } else {
            this.set(reforgeKey, PersistentDataType.STRING, value.id.key)
        }
    }

var ItemStack?.reforgeStone: Reforge?
    get() {
        this ?: return null
        return this.fast().persistentDataContainer.reforgeStone
    }
    set(value) {
        this ?: return
        this.fast().persistentDataContainer.reforgeStone = value
    }

var ItemMeta?.reforgeStone: Reforge?
    get() {
        this ?: return null
        return this.persistentDataContainer.reforgeStone
    }
    set(value) {
        this ?: return
        this.persistentDataContainer.reforgeStone = value
    }

var PersistentDataContainer?.reforgeStone: Reforge?
    get() {
        this ?: return null

        if (!this.has(reforgeStoneKey, PersistentDataType.STRING)) {
            return null
        }

        val active = this.get(reforgeStoneKey, PersistentDataType.STRING)
        return Reforges.getByKey(active)
    }
    set(value) {
        this ?: return
        if (value == null) {
            this.remove(reforgeStoneKey)
        } else {
            this.set(reforgeStoneKey, PersistentDataType.STRING, value.id.key)
        }
    }

var ItemStack.timesReforged: Int
    get() = this.fast().persistentDataContainer.get(reforgeAmountKey, PersistentDataType.INTEGER) ?: 0
    set(value) = this.fast().persistentDataContainer.set(reforgeAmountKey, PersistentDataType.INTEGER, value)

fun Collection<ReforgeTarget>.getRandomReforge(
    disallowed: Collection<Reforge> = emptyList()
): Reforge? {
    val applicable = mutableListOf<Reforge>()

    for (reforge in Reforges.values()) {
        if (reforge.targets.intersect(this.toSet()).isNotEmpty() && !reforge.requiresStone) {
            applicable.add(reforge)
        }
    }

    applicable.removeAll(disallowed)
    return applicable.randomOrNull()
}
