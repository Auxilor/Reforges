package com.willfp.reforges.effects.effects;

import com.willfp.eco.core.config.interfaces.JSONConfig;
import com.willfp.reforges.effects.Effect;
import com.willfp.reforges.vault.EconomyHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class EffectRewardBlockBreak extends Effect {
    /**
     * Create a new effect.
     */
    public EffectRewardBlockBreak() {
        super("reward_kill");
    }

    @Override
    public void onBlockBreak(@NotNull final Player player,
                             @NotNull final Block block,
                             @NotNull final BlockBreakEvent event,
                             @NotNull final JSONConfig config) {
        EconomyHandler.getInstance().depositPlayer(player, config.getDouble("amount"));
    }
}
