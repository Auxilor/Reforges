package com.willfp.reforges.reforges.util

import com.willfp.eco.core.items.args.LookupArgParser
import com.willfp.reforges.reforges.Reforges
import com.willfp.reforges.util.reforge
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.function.Predicate

object ReforgeArgParser : LookupArgParser {
    /**
     * Also reads `reforge:<namespace>:<key>`, the format saved by older versions.
     */
    override fun parseArguments(
        args: Array<String>,
        meta: ItemMeta
    ): Predicate<ItemStack>? {
        val reforge = args
            .filter { arg -> arg.contains(':') && arg.substringBefore(':').equals("reforge", ignoreCase = true) }
            .firstNotNullOfOrNull { arg ->
                val key = arg.substringAfter(':').lowercase()
                Reforges.getByKey(key) ?: Reforges.getByKey(key.substringAfterLast(':'))
            }
            ?: return null

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
