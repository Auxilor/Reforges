package com.willfp.reforges.reforges.reforges;

import com.willfp.eco.core.events.EntityDeathByEntityEvent;
import com.willfp.eco.util.NumberUtils;
import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.Reforges;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import com.willfp.reforges.reforges.util.ReforgeUtils;
import com.willfp.reforges.vault.EconomyHandler;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.jetbrains.annotations.NotNull;

public class ReforgeDurable extends Reforge {
    public ReforgeDurable() {
        super("durable");
    }

    @Override
    public ReforgeTarget[] getTarget() {
        return new ReforgeTarget[]{
                ReforgeTarget.TRIDENT,
                ReforgeTarget.ARMOR,
                ReforgeTarget.BOW,
                ReforgeTarget.MELEE,
                ReforgeTarget.PICKAXE
        };
    }

    @EventHandler
    public void handle(@NotNull final PlayerItemDamageEvent event) {
        Reforge reforge = ReforgeUtils.getReforge(event.getItem());

        if (reforge == null || !reforge.equals(this)) {
            return;
        }

        if (NumberUtils.randFloat(0, 100) < this.getConfig().getDouble("percent-less-damage")) {
            event.setCancelled(true);
        }
    }
}
