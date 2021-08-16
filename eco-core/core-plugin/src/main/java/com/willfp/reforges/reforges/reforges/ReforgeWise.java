package com.willfp.reforges.reforges.reforges;

import com.willfp.eco.core.events.NaturalExpGainEvent;
import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.Reforges;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import com.willfp.reforges.reforges.util.ReforgeUtils;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public class ReforgeWise extends Reforge {
    public ReforgeWise() {
        super("wise");
    }

    @Override
    public ReforgeTarget[] getTarget() {
        return new ReforgeTarget[]{ReforgeTarget.MELEE};
    }

    @EventHandler
    public void onExpChange(@NotNull final NaturalExpGainEvent event) {
        Reforge reforge = ReforgeUtils.getReforge(event.getExpChangeEvent().getPlayer().getInventory().getItemInMainHand());

        if (reforge == null || !reforge.equals(this)) {
            return;
        }

        event.getExpChangeEvent().setAmount((int) Math.ceil(event.getExpChangeEvent().getAmount() * this.getConfig().getDouble(Reforges.CONFIG_LOCATION + "multiplier")));
    }
}
