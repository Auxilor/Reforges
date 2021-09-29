package com.willfp.reforges.effects.effects;

import com.willfp.eco.core.config.interfaces.JSONConfig;
import com.willfp.eco.core.events.EntityDeathByEntityEvent;
import com.willfp.reforges.effects.Effect;
import com.willfp.reforges.vault.EconomyHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EffectRewardKill extends Effect {
    /**
     * Create a new effect.
     */
    public EffectRewardKill() {
        super("reward_kill");
    }

    @Override
    public void onKill(@NotNull final LivingEntity killer,
                       @NotNull final LivingEntity victim,
                       @NotNull final EntityDeathByEntityEvent event,
                       @NotNull final JSONConfig config) {
        if (!(killer instanceof Player player)) {
            return;
        }

        EconomyHandler.getInstance().depositPlayer(player, config.getDouble("amount"));
    }
}
