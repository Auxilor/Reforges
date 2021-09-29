package com.willfp.reforges.effects.effects

import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.reforges.effects.Effect
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageEvent

class EffectIncomingDamageMultiplier : Effect("incoming_damage_multiplier") {
    override fun onDamageWearingArmor(
        victim: LivingEntity,
        event: EntityDamageEvent,
        config: JSONConfig
    ) {
        event.damage *= config.getDouble("multiplier")
    }
}