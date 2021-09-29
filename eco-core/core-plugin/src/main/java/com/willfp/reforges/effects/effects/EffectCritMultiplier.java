package com.willfp.reforges.effects.effects;

import com.willfp.eco.core.config.interfaces.JSONConfig;
import com.willfp.reforges.effects.Effect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class EffectCritMultiplier extends Effect {
    /**
     * Create a new effect.
     */
    public EffectCritMultiplier() {
        super("crit_multiplier");
    }

    @Override
    public void onAnyDamage(@NotNull final LivingEntity attacker,
                            @NotNull final LivingEntity victim,
                            @NotNull final EntityDamageByEntityEvent event,
                            @NotNull final JSONConfig config) {
        if (attacker.getVelocity().getY() > 0) {
            return;
        }

        event.setDamage(event.getDamage() * config.getDouble("multiplier"));
    }
}
