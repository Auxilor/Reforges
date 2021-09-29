package com.willfp.reforges.effects.effects

import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.reforges.effects.Effect
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent

class EffectCritMultiplier : Effect("crit_multiplier") {
    override fun onAnyDamage(
        attacker: LivingEntity,
        victim: LivingEntity,
        event: EntityDamageByEntityEvent,
        config: JSONConfig
    ) {
        if (attacker.velocity.y > 0) {
            return
        }
        event.damage *= config.getDouble("multiplier")
    }
}