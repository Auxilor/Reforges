package com.willfp.reforges.reforges.reforges;

import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.Reforges;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class ReforgePointy extends Reforge {
    public ReforgePointy() {
        super("pointy");
    }

    @Override
    public ReforgeTarget[] getTarget() {
        return new ReforgeTarget[]{ReforgeTarget.TRIDENT};
    }

    @Override
    public void onTridentDamage(@NotNull final LivingEntity attacker,
                                @NotNull final LivingEntity victim,
                                @NotNull final Trident trident,
                                @NotNull final EntityDamageByEntityEvent event) {
        event.setDamage(event.getDamage() * this.getConfig().getDouble(Reforges.CONFIG_LOCATION + "multiplier"));
    }
}
