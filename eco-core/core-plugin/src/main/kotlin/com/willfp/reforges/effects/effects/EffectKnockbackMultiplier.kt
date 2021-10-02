package com.willfp.reforges.effects.effects

import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.reforges.effects.Effect
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.meta.ItemMeta

class EffectKnockbackMultiplier : Effect("knockback_multiplier") {
    override fun handleEnabling(
        meta: ItemMeta,
        config: JSONConfig
    ) {
        meta.addAttributeModifier(
            Attribute.GENERIC_ATTACK_KNOCKBACK,
            AttributeModifier(
                this.getUUID(1),
                this.id,
                config.getDouble("multiplier") - 1,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
            )
        )
    }

    override fun handleDisabling(meta: ItemMeta) {
        meta.removeAttributeModifier(
            Attribute.GENERIC_ATTACK_KNOCKBACK,
            AttributeModifier(
                this.getUUID(1),
                this.id,
                0.0,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
            )
        )
    }
}