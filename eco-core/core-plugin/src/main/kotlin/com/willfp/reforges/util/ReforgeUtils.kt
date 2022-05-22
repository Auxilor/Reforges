package com.willfp.reforges.util

import com.willfp.eco.core.fast.FastItemStack
import com.willfp.eco.core.items.TestableItem
import com.willfp.reforges.ReforgesPlugin
import com.willfp.reforges.reforges.Reforge
import com.willfp.reforges.reforges.Reforges.getByKey
import com.willfp.reforges.reforges.Reforges.values
import com.willfp.reforges.reforges.ReforgeTarget
import com.willfp.reforges.reforges.util.MetadatedReforgeStatus
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

object ReforgeUtils {
    private val PLUGIN = ReforgesPlugin.instance

    /**
     * The key for storing reforges.
     */
    private val REFORGE_KEY = PLUGIN.namespacedKeyFactory.create("reforge")

    /**
     * The key for storing reforge amounts.
     */
    private val REFORGE_AMOUNT = PLUGIN.namespacedKeyFactory.create("reforge_amount")

    /**
     * The key for storing reforge stones.
     */
    private val REFORGE_STONE_KEY = PLUGIN.namespacedKeyFactory.create("reforge_stone")

    /**
     * Get a random reforge for a target.
     *
     * @param targets    The targets.
     */
    fun getRandomReforge(targets: Collection<ReforgeTarget>): Reforge? {
        return getRandomReforge(targets, emptyList())
    }

    /**
     * Get a random reforge for a target.
     *
     * @param targets    The targets.
     * @param disallowed The disallowed reforges.
     */
    @JvmStatic
    fun getRandomReforge(
        targets: Collection<ReforgeTarget>,
        disallowed: Collection<Reforge>
    ): Reforge? {
        val applicable = mutableListOf<Reforge>()
        for (reforge in values()) {
            for (target in targets) {
                if (reforge.targets.contains(target) && !reforge.requiresStone) {
                    applicable.add(reforge)
                }
            }
        }
        applicable.shuffle()

        applicable.removeAll(disallowed)
        return if (applicable.isEmpty()) {
            null
        } else applicable[0]
    }

    fun getStatus(captive: List<ItemStack?>): MetadatedReforgeStatus {
        val toReforge = if (captive.isEmpty()) null else captive[0]
        val stone = if (captive.size == 2) captive[1] else null
        var status: ReforgeStatus? = null
        val target: MutableList<ReforgeTarget> = ArrayList()
        if (toReforge == null || toReforge.type == Material.AIR) {
            status = ReforgeStatus.NO_ITEM
        } else {
            target.addAll(ReforgeTarget.getForItem(toReforge))
            if (target.isEmpty()) {
                status = ReforgeStatus.INVALID_ITEM
            }
        }
        if (target.isNotEmpty()) {
            status = ReforgeStatus.ALLOW
        }
        var cost = 0.0
        if (status == ReforgeStatus.ALLOW) {
            val reforgeStone = getReforgeStone(stone)
            if (reforgeStone != null && reforgeStone.targets.stream()
                    .anyMatch { reforgeTarget: ReforgeTarget ->
                        reforgeTarget.items.stream()
                            .anyMatch { item: TestableItem -> item.matches(toReforge) }
                    }
            ) {
                cost = reforgeStone.stonePrice.toDouble()
                status = ReforgeStatus.ALLOW_STONE
            }
        }
        return MetadatedReforgeStatus(status!!, cost)
    }

    /**
     * Get reforge on an item.
     *
     * @param item The item to query.
     * @return The found reforge, or null.
     */
    fun getReforge(item: ItemStack?): Reforge? {
        if (item == null) {
            return null
        }
        val container = FastItemStack.wrap(item).persistentDataContainer
        return getReforge(container)
    }

    /**
     * Get reforge on an item.
     *
     * @param meta The item to query.
     * @return The found reforge, or null.
     */
    fun getReforge(meta: ItemMeta?): Reforge? {
        if (meta == null) {
            return null
        }
        val container = meta.persistentDataContainer
        if (!container.has(REFORGE_KEY, PersistentDataType.STRING)) {
            return null
        }
        val active = container.get(REFORGE_KEY, PersistentDataType.STRING)
        return getByKey(active)
    }

    /**
     * Get reforge on an item.
     *
     * @param container The item to query.
     * @return The found reforge, or null.
     */
    fun getReforge(container: PersistentDataContainer?): Reforge? {
        if (container == null) {
            return null
        }
        if (!container.has(REFORGE_KEY, PersistentDataType.STRING)) {
            return null
        }
        val active = container.get(REFORGE_KEY, PersistentDataType.STRING)
        return getByKey(active)
    }

    /**
     * Set reforge on an item.
     *
     * @param item    The item.
     * @param reforge The reforge.
     */
    fun setReforge(
        item: ItemStack,
        reforge: Reforge
    ) {
        if (item.itemMeta == null) {
            return
        }
        val meta = item.itemMeta
        setReforge(meta, reforge)
        item.itemMeta = meta
    }

    /**
     * Set reforge on an item.
     *
     * @param meta    The meta.
     * @param reforge The reforge.
     */
    fun setReforge(
        meta: ItemMeta,
        reforge: Reforge
    ) {
        val container = meta.persistentDataContainer
        container.set(REFORGE_KEY, PersistentDataType.STRING, reforge.id)
    }

    /**
     * Get reforge stone on an item.
     *
     * @param item The item to query.
     * @return The found reforge, or null.
     */
    fun getReforgeStone(item: ItemStack?): Reforge? {
        if (item == null) {
            return null
        }
        val container = FastItemStack.wrap(item).persistentDataContainer
        return getReforgeStone(container)
    }

    /**
     * Get reforge stone on an item.
     *
     * @param meta The item to query.
     * @return The found reforge, or null.
     */
    fun getReforgeStone(meta: ItemMeta?): Reforge? {
        if (meta == null) {
            return null
        }
        val container = meta.persistentDataContainer
        if (!container.has(REFORGE_STONE_KEY, PersistentDataType.STRING)) {
            return null
        }
        val active = container.get(REFORGE_STONE_KEY, PersistentDataType.STRING)
        return getByKey(active)
    }

    /**
     * Get reforge stone on an item.
     *
     * @param container The item to query.
     * @return The found reforge, or null.
     */
    fun getReforgeStone(container: PersistentDataContainer?): Reforge? {
        if (container == null) {
            return null
        }
        if (!container.has(REFORGE_STONE_KEY, PersistentDataType.STRING)) {
            return null
        }
        val active = container.get(REFORGE_STONE_KEY, PersistentDataType.STRING)
        return getByKey(active)
    }

    /**
     * Set an item to be a reforge stone.
     *
     * @param item    The item.
     * @param reforge The reforge.
     */
    fun setReforgeStone(
        item: ItemStack,
        reforge: Reforge
    ) {
        if (item.itemMeta == null) {
            return
        }
        val meta = item.itemMeta
        val container = meta.persistentDataContainer
        container.set(REFORGE_STONE_KEY, PersistentDataType.STRING, reforge.id)
        item.itemMeta = meta
    }

    /**
     * Get the amount of reforges done to an item.
     *
     * @param item The item.
     */
    @JvmStatic
    fun getReforges(item: ItemStack): Int {
        val meta = item.itemMeta ?: return 0
        val container = meta.persistentDataContainer
        if (!container.has(REFORGE_AMOUNT, PersistentDataType.INTEGER)) {
            container.set(REFORGE_AMOUNT, PersistentDataType.INTEGER, 0)
            item.itemMeta = meta
        }
        val amount = container.get(REFORGE_AMOUNT, PersistentDataType.INTEGER)
        return amount ?: 0
    }

    /**
     * Get the amount of reforges done to an item.
     *
     * @param item The item.
     */
    @JvmStatic
    fun incrementReforges(item: ItemStack) {
        val meta = item.itemMeta ?: return
        var amount = getReforges(item)
        amount++
        val container = meta.persistentDataContainer
        container.set(REFORGE_AMOUNT, PersistentDataType.INTEGER, amount)
        item.itemMeta = meta
    }
}