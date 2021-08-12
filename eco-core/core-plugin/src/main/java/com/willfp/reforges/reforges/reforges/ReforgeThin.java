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

import java.util.UUID;

public class ReforgeThin extends Reforge {
    private AttributeModifier speedModifier;

    public ReforgeThin() {
        super("thin");
    }

    @Override
    protected void postUpdate() {
        this.speedModifier = new AttributeModifier(
                UUID.nameUUIDFromBytes("thin-speed".getBytes()),
                "thin-speed",
                this.getConfig().getDouble(Reforges.CONFIG_LOCATION + "speed-multiplier") - 1,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
        );
    }

    @Override
    public ReforgeTarget getTarget() {
        return ReforgeTarget.ARMOR;
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

        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, speedModifier);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(meta);
    }

    @Override
    public void handleRemoval(@NotNull final ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;

        meta.removeAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, speedModifier);
        itemStack.setItemMeta(meta);
    }
}
