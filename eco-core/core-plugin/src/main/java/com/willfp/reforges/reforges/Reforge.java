package com.willfp.reforges.reforges;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.Prerequisite;
import com.willfp.eco.core.items.builder.SkullBuilder;
import com.willfp.eco.core.recipe.Recipes;
import com.willfp.reforges.ReforgesPlugin;
import com.willfp.reforges.config.ReforgeConfig;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import com.willfp.reforges.reforges.util.Watcher;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class Reforge implements Listener, Watcher {
    /**
     * Instance of Reforges for reforges to be able to access.
     */
    @Getter(AccessLevel.PROTECTED)
    private final EcoPlugin plugin = ReforgesPlugin.getInstance();

    /**
     * The key to store reforges in meta.
     */
    @Getter
    private final String key;

    /**
     * The reforges config.
     */
    @Getter
    private final ReforgeConfig config;

    /**
     * If the reforge is enabled.
     */
    @Getter
    private boolean enabled;

    /**
     * The display name.
     */
    @Getter
    private String name;

    /**
     * The display name.
     */
    @Getter
    private String description;

    /**
     * If a reforge stone is required.
     */
    @Getter
    private boolean requiresStone;

    /**
     * The reforge stone.
     */
    @Getter
    private ItemStack stone;

    /**
     * Create a new Reforge.
     *
     * @param key           The key name of the reforge..
     * @param prerequisites Optional {@link Prerequisite}s that must be met.
     */
    protected Reforge(@NotNull final String key,
                      @NotNull final Prerequisite... prerequisites) {
        this.key = key;
        this.config = new ReforgeConfig(this.getKey(), this.getClass(), this.plugin);

        if (!Prerequisite.areMet(prerequisites)) {
            return;
        }

        Reforges.addNewReforge(this);
        this.update();
    }

    /**
     * Update the reforge based off config values.
     * This can be overridden but may lead to unexpected behavior.
     */
    public void update() {
        enabled = config.getBool("enabled");
        name = config.getString("name");
        description = config.getString("description");

        requiresStone = config.getBool("stone-config.requires-stone");
        stone = new SkullBuilder()
                .setSkullTexture(config.getString("stone-config.texture"))
                .build();

        if (config.getBool("stone-config.craftable")) {
            Recipes.createAndRegisterRecipe(
                    plugin,
                    "stone_" + this.getKey(),
                    stone,
                    config.getStrings("stone-config.recipe", false)
            );
        }

        postUpdate();
    }

    protected void postUpdate() {
        // Unused as some talismans may have postUpdate tasks, however most won't.
    }

    /**
     * Get the reforge target.
     */
    public abstract ReforgeTarget getTarget();

    public void handleApplication(@NotNull final ItemStack itemStack) {
        // Override when needed
    }

    public void handleRemoval(@NotNull final ItemStack itemStack) {
        // Override when needed
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Reforge reforge)) {
            return false;
        }

        return this.getKey().equals(reforge.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getKey());
    }

    @Override
    public String toString() {
        return "Reforge{"
                + this.getKey()
                + "}";
    }
}
