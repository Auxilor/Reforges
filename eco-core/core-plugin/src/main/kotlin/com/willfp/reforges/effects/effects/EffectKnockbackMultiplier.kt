package com.willfp.reforges.effects.effects

import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.reforges.effects.Effect
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player

class EffectKnockbackMultiplier : Effect("knockback_multiplier") {
    override fun handleEnabling(player: Player,
                                config: JSONConfig) {
        val attribute = player.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK) ?: return
        attribute.addModifier(
            AttributeModifier(
                this.getUUID(1),
                this.id,
                config.getDouble("multiplier") - 1,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
            )
        )
    }

    override fun handleDisabling(player: Player) {
        val attribute = player.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK) ?: return
        attribute.removeModifier(
            AttributeModifier(
                this.getUUID(1),
                this.id,
                0.0,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
            )
        )
    }
}