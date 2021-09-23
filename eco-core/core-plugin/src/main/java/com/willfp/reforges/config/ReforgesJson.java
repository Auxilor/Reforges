package com.willfp.reforges.config;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.json.JSONBaseConfig;
import org.jetbrains.annotations.NotNull;

public class ReforgesJson extends JSONBaseConfig {
    /**
     * Instantiate reforges.json.
     *
     * @param plugin Instance of reforges.
     */
    public ReforgesJson(@NotNull final EcoPlugin plugin) {
        super("reforges", true, plugin);
    }
}
