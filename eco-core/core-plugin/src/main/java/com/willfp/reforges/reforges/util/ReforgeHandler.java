package com.willfp.reforges.reforges.util;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.eco.core.gui.menu.Menu;
import com.willfp.eco.core.gui.slot.Slot;
import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import com.willfp.reforges.vault.EconomyHandler;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ReforgeHandler extends PluginDependent<EcoPlugin> {
    /**
     * Pass an {@link EcoPlugin} in order to interface with it.
     *
     * @param plugin The plugin to manage.
     */
    public ReforgeHandler(@NotNull EcoPlugin plugin) {
        super(plugin);
    }

    public void handleReforgeClick(@NotNull final InventoryClickEvent event,
                                   @NotNull final Slot slot,
                                   @NotNull final Menu menu) {
        Player player = (Player) event.getWhoClicked();
        ItemStack toReforge = menu.getCaptiveItems(player).isEmpty() ? null : menu.getCaptiveItems(player).get(0);
        if (toReforge == null) {
            return;
        }

        ReforgeTarget target = ReforgeTarget.getForItem(toReforge);
        assert target != null;

        Reforge reforge = null;
        boolean usedStone = false;

        if (menu.getCaptiveItems(player).size() == 2) {
            Reforge stone = ReforgeUtils.getReforgeStone(menu.getCaptiveItems(player).get(1));
            if (stone != null) {
                if (Arrays.stream(stone.getTarget()).anyMatch(reforgeTarget -> reforgeTarget.getItems().contains(toReforge.getType()))) {
                    reforge = stone;
                    usedStone = true;
                }
            }
        }

        if (reforge == null) {
            reforge = ReforgeUtils.getRandomReforge(target);
        }

        if (reforge == null) {
            return;
        }


        if (EconomyHandler.isEnabled()) {
            double cost = this.getPlugin().getConfigYml().getDouble("reforge.cost");
            int reforges = ReforgeUtils.getReforges(toReforge);
            cost *= Math.pow(this.getPlugin().getConfigYml().getDouble("reforge.cost-exponent"), reforges);
            if (!EconomyHandler.getInstance().has(player, cost)) {
                player.sendMessage(this.getPlugin().getLangYml().getMessage("insufficient-money"));

                player.playSound(
                        player.getLocation(),
                        Sound.valueOf(this.getPlugin().getConfigYml().getString("gui.insufficient-money-sound.id").toUpperCase()),
                        1f,
                        (float) this.getPlugin().getConfigYml().getDouble("gui.insufficient-money-sound.pitch")
                );

                return;
            }


            EconomyHandler.getInstance().withdrawPlayer(player, cost);
        }

        int xpCost = this.getPlugin().getConfigYml().getInt("reforge.xp-cost");
        int reforges = ReforgeUtils.getReforges(toReforge);
        xpCost *= Math.pow(this.getPlugin().getConfigYml().getDouble("reforge.cost-exponent"), reforges);
        if (player.getLevel() < xpCost) {
            player.sendMessage(this.getPlugin().getLangYml().getMessage("insufficient-xp"));

            player.playSound(
                    player.getLocation(),
                    Sound.valueOf(this.getPlugin().getConfigYml().getString("gui.insufficient-money-sound.id").toUpperCase()),
                    1f,
                    (float) this.getPlugin().getConfigYml().getDouble("gui.insufficient-money-sound.pitch")
            );

            return;
        }

        player.setLevel(player.getLevel() - xpCost);

        player.sendMessage(this.getPlugin().getLangYml().getMessage("applied-reforge").replace("%reforge%", reforge.getName()));

        ReforgeUtils.incrementReforges(toReforge);

        ReforgeUtils.setReforge(toReforge, reforge);

        if (usedStone) {
            ItemStack stone = menu.getCaptiveItems(player).get(1);
            stone.setItemMeta(null);
            stone.setAmount(0);

            player.playSound(
                    player.getLocation(),
                    Sound.valueOf(this.getPlugin().getConfigYml().getString("gui.stone-sound.id").toUpperCase()),
                    1f,
                    (float) this.getPlugin().getConfigYml().getDouble("gui.stone-sound.pitch")
            );
        }

        player.playSound(
                player.getLocation(),
                Sound.valueOf(this.getPlugin().getConfigYml().getString("gui.sound.id").toUpperCase()),
                1f,
                (float) this.getPlugin().getConfigYml().getDouble("gui.sound.pitch")
        );
    }
}
