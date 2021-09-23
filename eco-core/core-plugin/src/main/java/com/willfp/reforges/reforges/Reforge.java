package com.willfp.reforges.reforges;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.interfaces.JSONConfig;
import com.willfp.eco.core.display.Display;
import com.willfp.eco.core.items.CustomItem;
import com.willfp.eco.core.items.builder.SkullBuilder;
import com.willfp.eco.core.recipe.Recipes;
import com.willfp.reforges.ReforgesPlugin;
import com.willfp.reforges.effects.Effect;
import com.willfp.reforges.effects.Effects;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import com.willfp.reforges.reforges.util.ReforgeUtils;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Reforge {
    /**
     * Instance of Reforges for reforges to be able to access.
     */
    @Getter(AccessLevel.PROTECTED)
    private final EcoPlugin plugin = ReforgesPlugin.getInstance();

    /**
     * The key to store reforges in meta.
     */
    @Getter
    private final String id;

    /**
     * The reforges config.
     */
    @Getter
    private final JSONConfig config;

    /**
     * The effects.
     */
    @Getter
    private final Map<Effect, JSONConfig> effects;

    /**
     * The display name.
     */
    @Getter
    private final String name;

    /**
     * The display name.
     */
    @Getter
    private final String description;

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
     * The reforge target.
     */
    @Getter
    private final Set<ReforgeTarget> targets;

    /**
     * Create a new Reforge.
     *
     * @param config The config.
     * @param plugin Instance of reforges.
     */
    protected Reforge(@NotNull final JSONConfig config,
                      @NotNull final ReforgesPlugin plugin) {
        this.config = config;
        this.id = config.getString("id");

        this.name = config.getString("name");
        this.description = config.getString("description");

        this.targets = new HashSet<>();
        for (String targetName : config.getStrings("targets")) {
            this.targets.add(ReforgeTarget.getByName(targetName));
        }

        this.effects = new HashMap<>();
        for (JSONConfig effectConfig : config.getSubsections("effects")) {
            Effect effect = Effects.getByName(effectConfig.getString("id"));
            if (effect != null) {
                effects.put(effect, effectConfig);
            }
        }

        handleStone(config.getSubsection("stone"), plugin);
    }

    private void handleStone(@NotNull final JSONConfig config,
                             @NotNull final ReforgesPlugin plugin) {
        this.requiresStone = config.getBool("enabled");

        if (requiresStone) {
            List<String> stoneLore = new ArrayList<>();
            for (String string : this.getPlugin().getConfigYml().getStrings("reforge.stone.lore")) {
                stoneLore.add(Display.PREFIX + string.replace("%reforge%", this.getName()));
            }
            stone = new SkullBuilder()
                    .setSkullTexture(config.getString("texture"))
                    .setDisplayName(this.getPlugin().getConfigYml().getString("stone.name").replace("%reforge%", this.getName()))
                    .addLoreLines(stoneLore)
                    .build();
        } else {
            stone = new SkullBuilder()
                    .build();
        }

        ReforgeUtils.setReforgeStone(stone, this);

        new CustomItem(
                plugin.getNamespacedKeyFactory().create("stone_" + this.getId()),
                test -> Objects.equals(ReforgeUtils.getReforgeStone(test), this),
                stone
        ).register();

        if (config.getBool("craftable")) {
            Recipes.createAndRegisterRecipe(
                    plugin,
                    "stone_" + this.getId(),
                    stone,
                    config.getStrings("recipe", false)
            );
        }
    }

    public final void handleApplication(@NotNull final ItemStack itemStack) {
        itemStack.setItemMeta(this.handleApplication(Objects.requireNonNull(itemStack.getItemMeta())));
    }

    public final ItemMeta handleApplication(@NotNull final ItemMeta meta) {
        for (Map.Entry<Effect, JSONConfig> entry : this.getEffects().entrySet()) {
            entry.getKey().handleApplication(meta, entry.getValue());
        }

        // Override when needed
        return meta;
    }

    public final void handleRemoval(@NotNull final ItemStack itemStack) {
        itemStack.setItemMeta(this.handleRemoval(Objects.requireNonNull(itemStack.getItemMeta())));
    }

    public final ItemMeta handleRemoval(@NotNull final ItemMeta meta) {
        for (Map.Entry<Effect, JSONConfig> entry : this.getEffects().entrySet()) {
            entry.getKey().handleApplication(meta, entry.getValue());
        }

        return meta;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Reforge reforge)) {
            return false;
        }

        return this.getId().equals(reforge.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    @Override
    public String toString() {
        return "Reforge{"
                + this.getId()
                + "}";
    }
}
