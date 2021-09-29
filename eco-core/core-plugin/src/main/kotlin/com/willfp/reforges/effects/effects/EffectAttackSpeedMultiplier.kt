package com.willfp.reforges.effects.effects

import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.reforges.effects.Effect
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.meta.ItemMeta

class EffectAttackSpeedMultiplier : Effect("attack_speed_multiplier") {
    override fun handleApplication(
        meta: ItemMeta,
        config: JSONConfig
    ) {
        meta.addAttributeModifier(
            Attribute.GENERIC_ATTACK_SPEED,
            AttributeModifier(
                this.uuid,
                this.id,
                config.getDouble("multiplier") - 1,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
            )
        )
    }

    override fun handleRemoval(meta: ItemMeta) {
        meta.removeAttributeModifier(
            Attribute.GENERIC_ATTACK_SPEED,
            AttributeModifier(
                this.uuid,
                this.id,
                0.0,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
            )
        )
    }
}