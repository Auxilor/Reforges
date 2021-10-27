package com.willfp.reforges.reforges.util

import com.willfp.eco.core.items.args.LookupArgParser
import com.willfp.reforges.reforges.Reforge
import com.willfp.reforges.reforges.Reforges
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.function.Predicate

class ReforgeArgParser : LookupArgParser {
    override fun parseArguments(
        args: Array<String>,
        meta: ItemMeta
    ): Predicate<ItemStack>? {
        var reforge: Reforge? = null

        for (arg in args) {
            val split = arg.split(":").toTypedArray()
            if (split.size == 1 || !split[0].equals("reforge", ignoreCase = true)) {
                continue
            }
            val match = Reforges.getByKey(split[1].lowercase()) ?: continue
            reforge = match
            break
        }

        reforge ?: return null

        ReforgeUtils.setReforge(meta, reforge)

        return Predicate { test ->
            val testMeta = test.itemMeta ?: return@Predicate false
            reforge == ReforgeUtils.getReforge(testMeta)
        }
    }
}