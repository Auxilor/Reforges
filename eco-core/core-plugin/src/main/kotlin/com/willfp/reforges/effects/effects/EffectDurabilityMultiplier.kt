package com.willfp.reforges.effects.effects

import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.reforges.effects.Effect
import org.bukkit.event.player.PlayerItemDamageEvent

class EffectDurabilityMultiplier : Effect("damage_multiplier") {
    override fun onDurabilityDamage(
        event: PlayerItemDamageEvent,
        config: JSONConfig
    ) {
        val multiplier = config.getDouble("multiplier")
    }
}