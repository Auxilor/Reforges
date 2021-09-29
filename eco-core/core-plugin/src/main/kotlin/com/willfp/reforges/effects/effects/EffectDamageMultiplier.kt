package com.willfp.reforges.effects.effects

import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.reforges.effects.Effect
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent

class EffectDamageMultiplier : Effect("damage_multiplier") {
    override fun onAnyDamage(
        attacker: LivingEntity,
        victim: LivingEntity,
        event: EntityDamageByEntityEvent,
        config: JSONConfig
    ) {
        event.damage *= config.getDouble("multiplier")
    }
}