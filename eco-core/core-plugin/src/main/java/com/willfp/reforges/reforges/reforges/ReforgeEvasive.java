package com.willfp.reforges.reforges.reforges;

import com.willfp.eco.util.NumberUtils;
import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.Reforges;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class ReforgeEvasive extends Reforge {
    public ReforgeEvasive() {
        super("evasive");
    }

    @Override
    public ReforgeTarget getTarget() {
        return ReforgeTarget.ARMOR;
    }

    @Override
    public void onDamageWearingArmor(@NotNull final LivingEntity victim,
                                     @NotNull final EntityDamageEvent event) {
        if (NumberUtils.randFloat(0, 100) < this.getConfig().getDouble(Reforges.CONFIG_LOCATION + "chance")) {
            event.setCancelled(true);
        }
    }
}
