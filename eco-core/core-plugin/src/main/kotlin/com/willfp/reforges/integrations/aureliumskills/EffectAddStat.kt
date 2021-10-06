package com.willfp.reforges.integrations.aureliumskills

import com.archyx.aureliumskills.api.AureliumAPI
import com.archyx.aureliumskills.stats.Stats
import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.reforges.effects.Effect
import com.willfp.reforges.effects.getEffectAmount
import org.bukkit.entity.Player

class EffectAddStat : Effect("add_stat") {
    override fun handleEnable(
        player: Player,
        config: JSONConfig
    ) {
        AureliumAPI.addStatModifier(
            player,
            this.getNamespacedKey(player.getEffectAmount(this)).key,
            Stats.valueOf(config.getString("stat", false)),
            config.getDouble("amount")
        )
    }

    override fun handleDisable(player: Player) {
        AureliumAPI.removeStatModifier(
            player,
            this.getNamespacedKey(player.getEffectAmount(this)).key,
        )
    }
}