package com.willfp.reforges.config;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.BaseConfig;
import com.willfp.eco.core.config.ConfigType;
import com.willfp.eco.core.items.Items;
import com.willfp.eco.core.items.TestableItem;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TargetYml extends BaseConfig {
    /**
     * Instantiate target.yml.
     *
     * @param plugin Instance of EcoEnchants.
     */
    public TargetYml(@NotNull final EcoPlugin plugin) {
        super("target", plugin, false, ConfigType.YAML);
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
        this.getStrings(target + ".items", false).forEach(s -> items.add(Items.lookup(s.toUpperCase())));
        return items;
    }

    /**
     * Get all materials from a target name.
     *
     * @param target The name of the target.
     * @return All materials.
     */
    public ReforgeTarget.Slot getSlot(@NotNull final String target) {
        return ReforgeTarget.Slot.valueOf(this.getString(target + ".slot").toUpperCase());
    }
}