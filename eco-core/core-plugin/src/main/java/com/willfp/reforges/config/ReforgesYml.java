package com.willfp.reforges.config;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.yaml.YamlBaseConfig;
import org.jetbrains.annotations.NotNull;

public class ReforgesYml extends YamlBaseConfig {
    /**
     * Instantiate reforges.yml.
     *
     * @param plugin Instance of reforges.
     */
    public ReforgesYml(@NotNull final EcoPlugin plugin) {
        super("reforges", true, plugin);
    }
}
