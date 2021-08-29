package com.willfp.reforges.reforges.reforges;

import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.Reforges;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ReforgeTough extends Reforge {
    public ReforgeTough() {
        super("tough");
    }

    @Override
    public ReforgeTarget[] getTarget() {
        return new ReforgeTarget[]{ReforgeTarget.ARMOR};
    }

    @Override
    public void onDamageWearingArmor(@NotNull final LivingEntity victim,
                                     @NotNull final EntityDamageEvent event) {
        event.setDamage(event.getDamage() * this.getConfig().getDouble(Reforges.CONFIG_LOCATION + "multiplier"));
    }

    @Override
    public void handleApplication(@NotNull final ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;

        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(
                this.getUuid(),
                "tough-speed",
                this.getConfig().getDouble(Reforges.CONFIG_LOCATION + "speed-multiplier") - 1,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
        ));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(meta);
    }

    @Override
    public void handleRemoval(@NotNull final ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;

        meta.removeAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED);
        itemStack.setItemMeta(meta);
    }
}
