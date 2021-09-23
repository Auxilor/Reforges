package com.willfp.reforges.config;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.yaml.YamlBaseConfig;
import com.willfp.eco.core.items.Items;
import com.willfp.eco.core.items.TestableItem;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TargetYml extends YamlBaseConfig {
    /**
     * Instantiate target.yml.
     *
     * @param plugin Instance of EcoEnchants.
     */
    public TargetYml(@NotNull final EcoPlugin plugin) {
        super("target", false, plugin);
    }

    /**
     * Get all target names.
     *
     * @return Set of all names.
     */
    public List<String> getTargets() {
        return this.getKeys(false);
    }

    /**
     * Get all materials from a target name.
     *
     * @param target The name of the target.
     * @return All materials.
     */
    public Set<TestableItem> getTargetItems(@NotNull final String target) {
        Set<TestableItem> items = new HashSet<>();
        this.getStrings(target, false).forEach(s -> items.add(Items.lookup(s.toUpperCase())));
        return items;
    }
}