package com.willfp.reforges.reforges.reforges;

import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.Reforges;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ReforgeLight extends Reforge {
    public ReforgeLight() {
        super("light");
    }

    @Override
    public ReforgeTarget getTarget() {
        return ReforgeTarget.MELEE;
    }

    @Override
    public void onMeleeAttack(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              @NotNull final EntityDamageByEntityEvent event) {
        event.setDamage(event.getDamage() * this.getConfig().getDouble(Reforges.CONFIG_LOCATION + "multiplier"));
    }

    @Override
    public void handleApplication(@NotNull final ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;

        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                "light-speed",
                this.getConfig().getDouble(Reforges.CONFIG_LOCATION + "speed-multiplier") - 1,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
        ));
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_KNOCKBACK, new AttributeModifier(
                "light-kb",
                this.getConfig().getDouble(Reforges.CONFIG_LOCATION + "knockback-multiplier") - 1,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
        ));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(meta);
    }

    @Override
    public void handleRemoval(@NotNull final ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;

        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                "light-speed",
                this.getConfig().getDouble(Reforges.CONFIG_LOCATION + "speed-multiplier") - 1,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
        ));
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_KNOCKBACK, new AttributeModifier(
                "light-kb",
                this.getConfig().getDouble(Reforges.CONFIG_LOCATION + "knockback-multiplier") - 1,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
        ));
        itemStack.setItemMeta(meta);
    }
}
