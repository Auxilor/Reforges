package com.willfp.reforges.reforges.util;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.reforges.ReforgesPlugin;
import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.Reforges;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class ReforgeUtils {
    /**
     * Instance of StatTrackers.
     */
    private static final EcoPlugin PLUGIN = ReforgesPlugin.getInstance();

    /**
     * The key for storing the currently displayed stat.
     */
    private static final NamespacedKey REFORGE_KEY = PLUGIN.getNamespacedKeyFactory().create("reforge");

    /**
     * Get reforge on an item.
     *
     * @param item The item to query.
     * @return The found reforge, or null if none active.
     */
    public static Reforge getReforge(@Nullable final ItemStack item) {
        if (item == null) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return null;
        }

        return getReforge(meta);
    }

    /**
     * Get reforge on an item.
     *
     * @param meta The item to query.
     * @return The found reforge, or null if none active.
     */
    public static Reforge getReforge(@Nullable final ItemMeta meta) {
        if (meta == null) {
            return null;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();

        String active = container.get(REFORGE_KEY, PersistentDataType.STRING);

        return Reforges.getByKey(active);
    }

    /**
     * Set reforge on an item.
     *
     * @param item    The item.
     * @param reforge The reforge.
     */
    public static void setReforge(@NotNull final ItemStack item,
                                  @NotNull final Reforge reforge) {
        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return;
        }

        setReforge(meta, reforge);

        item.setItemMeta(meta);
    }

    /**
     * Set reforge on an item.
     *
     * @param meta    The meta.
     * @param reforge The reforge.
     */
    public static void setReforge(@NotNull final ItemMeta meta,
                                  @NotNull final Reforge reforge) {
        PersistentDataContainer container = meta.getPersistentDataContainer();

        container.set(REFORGE_KEY, PersistentDataType.STRING, reforge.getKey());
    }
}
