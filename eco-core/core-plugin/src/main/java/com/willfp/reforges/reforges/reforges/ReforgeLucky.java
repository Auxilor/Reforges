package com.willfp.reforges.reforges.reforges;

import com.willfp.eco.util.NumberUtils;
import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.Reforges;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class ReforgeLucky extends Reforge {
    public ReforgeLucky() {
        super("lucky");
    }

    @Override
    public ReforgeTarget[] getTarget() {
        return new ReforgeTarget[]{ReforgeTarget.BOW};
    }

    @Override
    public void onArrowDamage(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              @NotNull final Arrow arrow,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (NumberUtils.randFloat(0, 100) < this.getConfig().getDouble(Reforges.CONFIG_LOCATION + "chance")) {
            event.setDamage(event.getDamage() * this.getConfig().getDouble(Reforges.CONFIG_LOCATION + "multiplier"));
        } else {
            event.setDamage(event.getDamage() * this.getConfig().getDouble(Reforges.CONFIG_LOCATION + "bad-multiplier"));
        }
    }
}
