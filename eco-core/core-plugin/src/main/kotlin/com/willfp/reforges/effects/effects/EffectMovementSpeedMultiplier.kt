package com.willfp.reforges.effects.effects

import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.reforges.effects.Effect
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.ItemMeta

class EffectMovementSpeedMultiplier : Effect("movement_speed_multiplier") {
    override fun handleEnabling(
        meta: ItemMeta,
        config: JSONConfig
    ) {
        meta.addAttributeModifier(
            Attribute.GENERIC_MOVEMENT_SPEED,
            AttributeModifier(
                this.getUUID(1),
                this.id,
                config.getDouble("multiplier") - 1,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
            )
        )

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
    }

    override fun handleDisabling(meta: ItemMeta) {
        meta.removeAttributeModifier(
            Attribute.GENERIC_MOVEMENT_SPEED,
            AttributeModifier(
                this.getUUID(1),
                this.id,
                0.0,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
            )
        )
    }
}