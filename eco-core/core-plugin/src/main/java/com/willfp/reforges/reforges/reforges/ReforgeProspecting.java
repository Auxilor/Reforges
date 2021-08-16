package com.willfp.reforges.reforges.reforges;

import com.willfp.eco.util.NumberUtils;
import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.Reforges;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import com.willfp.reforges.vault.EconomyHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class ReforgeProspecting extends Reforge {
    public ReforgeProspecting() {
        super("prospecting");
    }

    @Override
    public ReforgeTarget[] getTarget() {
        return new ReforgeTarget[]{ReforgeTarget.PICKAXE};
    }

    @Override
    public void onBlockBreak(@NotNull final Player player,
                             @NotNull final Block block,
                             @NotNull final BlockBreakEvent event) {
        if (NumberUtils.randFloat(0, 100) < this.getConfig().getDouble(Reforges.CONFIG_LOCATION + "chance")) {
            EconomyHandler.getInstance().depositPlayer(player, this.getConfig().getDouble(Reforges.CONFIG_LOCATION + "money"));
        }
    }
}
