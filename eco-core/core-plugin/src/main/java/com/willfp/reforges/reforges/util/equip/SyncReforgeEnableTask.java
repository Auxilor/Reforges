package com.willfp.reforges.reforges.util.equip;

import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.ReforgeLookup;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SyncReforgeEnableTask {
    public static final Consumer<Player> CHECK = (player) -> {
        List<Reforge> before = ReforgeLookup.provideReforges(player);
        ReforgeLookup.clearCaches(player);
        List<Reforge> after = ReforgeLookup.provideReforges(player);
        List<Reforge> added = new ArrayList<>(after);
        added.removeAll(before);

        for (Reforge reforge : added) {
            reforge.handleActivation(player);
        }

        before.removeAll(after);

        for (Reforge reforge : before) {
            reforge.handleDeactivation(player);
        }
    };
}