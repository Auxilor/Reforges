package com.willfp.reforges.effects.effects

import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.reforges.effects.Effect
import com.willfp.reforges.effects.getEffectAmount
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player

class EffectAttackSpeedMultiplier : Effect("attack_speed_multiplier") {
    override fun handleEnable(player: Player, config: JSONConfig) {
        val attribute = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED) ?: return
        attribute.addModifier(
            AttributeModifier(
                this.getUUID(player.getEffectAmount(this)),
                this.id,
                config.getDouble("multiplier") - 1,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
            )
        )
    }

    override fun handleDisable(player: Player) {
        val attribute = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED) ?: return
        attribute.removeModifier(
            AttributeModifier(
                this.getUUID(player.getEffectAmount(this)),
                this.id,
                0.0,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
            )
        )
    }
}