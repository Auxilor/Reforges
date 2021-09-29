package com.willfp.reforges.effects.effects;

import com.willfp.eco.core.config.interfaces.JSONConfig;
import com.willfp.reforges.effects.Effect;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class EffectKnockbackMultiplier extends Effect {
    /**
     * Create a new effect.
     */
    public EffectKnockbackMultiplier() {
        super("knockback_multiplier");
    }

    @Override
    public void handleApplication(@NotNull final ItemMeta meta,
                                  @NotNull final JSONConfig config) {
        meta.addAttributeModifier(
                Attribute.GENERIC_ATTACK_KNOCKBACK,
                new AttributeModifier(
                        this.getUuid(),
                        this.getName(),
                        config.getDouble("multiplier") - 1,
                        AttributeModifier.Operation.MULTIPLY_SCALAR_1
                )
        );
    }

    @Override
    public void handleRemoval(@NotNull final ItemMeta meta) {
        meta.removeAttributeModifier(
                Attribute.GENERIC_ATTACK_KNOCKBACK,
                new AttributeModifier(
                        this.getUuid(),
                        this.getName(),
                        0,
                        AttributeModifier.Operation.MULTIPLY_SCALAR_1
                )
        );
    }
}
