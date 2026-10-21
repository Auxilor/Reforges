package com.willfp.reforges.reforges.util

import com.willfp.eco.core.items.args.LookupArgParser
import com.willfp.reforges.reforges.Reforge
import com.willfp.reforges.reforges.Reforges
import com.willfp.reforges.util.reforge
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.function.Predicate

object ReforgeArgParser : LookupArgParser {
    override fun parseArguments(
        args: Array<String>,
        meta: ItemMeta
    ): Predicate<ItemStack>? {
        var reforge: Reforge? = null

        for (arg in args) {
            if (!arg.contains(':') || !arg.substringBefore(':').equals("reforge", ignoreCase = true)) {
                continue
            }
            val key = arg.substringAfter(':').lowercase()
            // Strings saved before the fix read reforge:<namespace>:<key>.
            val match = Reforges.getByKey(key) ?: Reforges.getByKey(key.substringAfterLast(':')) ?: continue
            reforge = match
            break
        }

        reforge ?: return null

        meta.reforge = reforge

        return Predicate { test ->
            val testMeta = test.itemMeta ?: return@Predicate false
            reforge == testMeta.reforge
        }
    }

    override fun serializeBack(meta: ItemMeta): String? {
        val reforge = meta.reforge ?: return null

        return "reforge:${reforge.id.key}"
    }
}
