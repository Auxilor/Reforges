package com.willfp.reforges.config;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.yaml.YamlExtendableConfig;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class ReforgeConfig extends YamlExtendableConfig {
    /**
     * The name of the config.
     */
    @Getter
    private final String name;

    /**
     * Instantiate a new config for a reforge.
     *
     * @param name   The name of the config.
     * @param source The provider of the reforge.
     * @param plugin Instance of reforges.
     */
    public ReforgeConfig(@NotNull final String name,
                         @NotNull final Class<?> source,
                         @NotNull final EcoPlugin plugin) {
        super(name, true, plugin, source, "reforges/");
        this.name = name;
    }
}
