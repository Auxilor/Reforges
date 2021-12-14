package com.willfp.reforges.config;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.BaseConfig;
import com.willfp.eco.core.config.ConfigType;
import org.jetbrains.annotations.NotNull;

public class ReforgesYml extends BaseConfig {
    /**
     * Instantiate reforges.yml.
     *
     * @param plugin Instance of reforges.
     */
    public ReforgesYml(@NotNull final EcoPlugin plugin) {
        super("reforges", plugin, true, ConfigType.YAML);
    }
}
