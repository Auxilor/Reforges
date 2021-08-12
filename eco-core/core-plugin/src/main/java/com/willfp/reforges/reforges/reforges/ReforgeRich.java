package com.willfp.reforges.reforges.reforges;

import com.willfp.eco.core.events.EntityDeathByEntityEvent;
import com.willfp.eco.util.ArrowUtils;
import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.Reforges;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import com.willfp.reforges.reforges.util.ReforgeUtils;
import com.willfp.reforges.vault.EconomyHandler;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ReforgeRich extends Reforge {
    public ReforgeRich() {
        super("rich");
    }

    @Override
    public ReforgeTarget getTarget() {
        return ReforgeTarget.BOW;
    }

    @EventHandler
    public void onKill(@NotNull final EntityDeathByEntityEvent event) {
        if (!(event.getKiller() instanceof Arrow arrow)) {
            return;
        }

        if (!(arrow.getShooter() instanceof Player player)) {
            return;
        }

        ItemStack bow = ArrowUtils.getBow(arrow);

        if (bow == null) {
            return;
        }

        Reforge reforge = ReforgeUtils.getReforge(bow);

        if (reforge == null || !reforge.equals(this)) {
            return;
        }

        EconomyHandler.getInstance().depositPlayer(player, this.getConfig().getDouble(Reforges.CONFIG_LOCATION + "money"));
    }
}
