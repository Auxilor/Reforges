package com.willfp.reforges.effects.effects;

import com.willfp.eco.core.config.interfaces.JSONConfig;
import com.willfp.reforges.effects.Effect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class EffectIncomingDamageMultiplier extends Effect {
    /**
     * Create a new effect.
     */
    public EffectIncomingDamageMultiplier() {
        super("incoming_damage_multiplier");
    }

    @Override
    public void onDamageWearingArmor(@NotNull final LivingEntity victim,
                                     @NotNull final EntityDamageEvent event,
                                     @NotNull final JSONConfig config) {
        event.setDamage(event.getDamage() * config.getDouble("multiplier"));
    }
}
