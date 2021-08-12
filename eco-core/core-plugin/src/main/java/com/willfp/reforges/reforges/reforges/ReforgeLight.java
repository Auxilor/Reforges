package com.willfp.reforges.reforges.reforges;

import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.Reforges;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ReforgeLight extends Reforge {
    private AttributeModifier speedModifier;
    private AttributeModifier kbModifier;

    public ReforgeLight() {
        super("light");
    }

    @Override
    protected void postUpdate() {
        this.speedModifier = new AttributeModifier(
                UUID.nameUUIDFromBytes("light-speed".getBytes()),
                "light-speed",
                this.getConfig().getDouble(Reforges.CONFIG_LOCATION + "speed-multiplier"),
                AttributeModifier.Operation.MULTIPLY_SCALAR_1,
                EquipmentSlot.HAND
        );
        this.kbModifier = new AttributeModifier(
                UUID.nameUUIDFromBytes("light-kb".getBytes()),
                "light-kb",
                this.getConfig().getDouble(Reforges.CONFIG_LOCATION + "knockback-multiplier"),
                AttributeModifier.Operation.MULTIPLY_SCALAR_1,
                EquipmentSlot.HAND
        );
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

        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, speedModifier);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_KNOCKBACK, kbModifier);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(meta);
    }

    @Override
    public void handleRemoval(@NotNull final ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;

        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, speedModifier);
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_KNOCKBACK, kbModifier);
        itemStack.setItemMeta(meta);
    }
}
