package com.willfp.reforges.reforges.reforges;

import com.willfp.eco.core.integrations.anticheat.AnticheatManager;
import com.willfp.eco.util.NumberUtils;
import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import com.willfp.reforges.reforges.util.ReforgeUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.jetbrains.annotations.NotNull;

public class ReforgeImmediate extends Reforge {
    public ReforgeImmediate() {
        super("immediate");
    }

    @Override
    public ReforgeTarget[] getTarget() {
        return new ReforgeTarget[]{
                ReforgeTarget.PICKAXE
        };
    }

    @EventHandler(ignoreCancelled = true)
    public void handle(@NotNull final BlockDamageEvent event) {
        Reforge reforge = ReforgeUtils.getReforge(event.getItemInHand());

        if (reforge == null || !reforge.equals(this)) {
            return;
        }

        Block block = event.getBlock();

        if (block.getDrops(event.getItemInHand()).isEmpty()) {
            return;
        }

        if (block.getType().getHardness() > 100) {
            return;
        }

        if (block.getType() == Material.BEDROCK) {
            return;
        }

        AnticheatManager.exemptPlayer(event.getPlayer());

        if (NumberUtils.randFloat(0, 100) < this.getConfig().getDouble("chance")) {
            event.setInstaBreak(true);
        }

        AnticheatManager.unexemptPlayer(event.getPlayer());
    }
}
