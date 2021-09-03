package com.willfp.reforges.reforges.util;

import com.willfp.eco.core.items.args.LookupArgParser;
import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.Reforges;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class ReforgeArgParser implements LookupArgParser {
    @Override
    public Predicate<ItemStack> parseArguments(@NotNull final String[] args,
                                               @NotNull final ItemMeta meta) {
        Reforge reforge = null;
        for (String arg : args) {
            String[] split = arg.split(":");
            if (split.length == 1 || !split[0].equalsIgnoreCase("reforge")) {
                continue;
            }

            Reforge match = Reforges.getByKey(split[1].toLowerCase());
            if (match == null) {
                continue;
            }

            reforge = match;
            break;
        }

        if (reforge == null) {
            return itemStack -> true;
        }

        ReforgeUtils.setReforge(meta, reforge);

        Reforge finalReforge = reforge;
        return test -> {
            ItemMeta testMeta = test.getItemMeta();
            if (testMeta == null) {
                return false;
            }

            return finalReforge.equals(ReforgeUtils.getReforge(testMeta));
        };
    }
}
