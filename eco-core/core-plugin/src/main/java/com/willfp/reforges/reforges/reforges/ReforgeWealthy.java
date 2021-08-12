package com.willfp.reforges.reforges.reforges;

import com.willfp.eco.core.events.EntityDeathByEntityEvent;
import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.Reforges;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import com.willfp.reforges.reforges.util.ReforgeUtils;
import com.willfp.reforges.vault.EconomyHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public class ReforgeWealthy extends Reforge {
    public ReforgeWealthy() {
        super("wealthy");
    }

    @Override
    public ReforgeTarget getTarget() {
        return ReforgeTarget.MELEE;
    }

    @EventHandler
    public void onKill(@NotNull final EntityDeathByEntityEvent event) {
        if (!(event.getKiller() instanceof Player player)) {
            return;
        }

        Reforge reforge = ReforgeUtils.getReforge(player.getInventory().getItemInMainHand());

        if (reforge == null || !reforge.equals(this)) {
            return;
        }

        EconomyHandler.getInstance().depositPlayer(player, this.getConfig().getDouble(Reforges.CONFIG_LOCATION + "money"));
    }
}
