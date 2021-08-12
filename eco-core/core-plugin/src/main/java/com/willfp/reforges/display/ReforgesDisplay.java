package com.willfp.reforges.display;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.display.DisplayModule;
import com.willfp.eco.core.display.DisplayPriority;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ReforgesDisplay extends DisplayModule {
    /**
     * Create weapons display.
     *
     * @param plugin Instance of Reforges.
     */
    public ReforgesDisplay(@NotNull final EcoPlugin plugin) {
        super(plugin, DisplayPriority.HIGHEST);
    }

    @Override
    protected void display(@NotNull final ItemStack itemStack,
                           @NotNull final Object... args) {
    }
}
