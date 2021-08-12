package com.willfp.reforges.gui;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.updating.ConfigUpdater;
import com.willfp.eco.core.gui.menu.Menu;
import com.willfp.eco.core.gui.slot.FillerMask;
import com.willfp.eco.core.gui.slot.MaskMaterials;
import com.willfp.eco.core.gui.slot.Slot;
import com.willfp.eco.core.items.builder.ItemStackBuilder;
import com.willfp.eco.util.NumberUtils;
import com.willfp.reforges.ReforgesPlugin;
import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import com.willfp.reforges.reforges.util.ReforgeStatus;
import com.willfp.reforges.reforges.util.ReforgeUtils;
import com.willfp.reforges.vault.EconomyHandler;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class ReforgeGUI {
    /**
     * The reforge GUI.
     */
    @Getter
    private static Menu menu;

    /**
     * Update the GUI.
     *
     * @param plugin The plugin.
     */
    @SuppressWarnings("checkstyle:MissingSwitchDefault")
    @ConfigUpdater
    public static void update(@NotNull final EcoPlugin plugin) {
        menu = Menu.builder(6)
                .setTitle("Reforge Item")
                .setMask(
                        new FillerMask(
                                new MaskMaterials(
                                        Material.BLACK_STAINED_GLASS_PANE,
                                        Material.MAGENTA_STAINED_GLASS_PANE
                                ),
                                "011111110",
                                "012202210",
                                "012111210",
                                "010111010",
                                "011111110",
                                "011101110"
                        )
                ).modfiy(builder -> {
                    Slot slot = Slot.builder(
                            new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE)
                                    .setDisplayName("&r")
                                    .build()
                    ).setModifier((player, menu, previous) -> {

                        ReforgeStatus status = ReforgeUtils.getStatus(menu.getCaptiveItems(player));
                        if (status == ReforgeStatus.ALLOW || status == ReforgeStatus.ALLOW_STONE) {
                            previous.setType(Material.LIME_STAINED_GLASS_PANE);
                        } else {
                            previous.setType(Material.RED_STAINED_GLASS_PANE);
                        }
                    }).build();

                    for (int i = 1; i <= 6; i++) {
                        builder.setSlot(i, 1, slot);
                        builder.setSlot(i, 9, slot);
                    }
                }).setSlot(4, 3,
                        Slot.builder()
                                .setCaptive()
                                .build()
                ).setSlot(4, 7,
                        Slot.builder()
                                .setCaptive()
                                .build()
                ).setSlot(2, 5,
                        Slot.builder(new ItemStack(Material.ANVIL))
                                .setModifier((player, menu, previous) -> {
                                    ItemMeta meta = previous.getItemMeta();
                                    if (meta == null) {
                                        return;
                                    }

                                    ReforgeStatus status = ReforgeUtils.getStatus(menu.getCaptiveItems(player));

                                    double cost = plugin.getConfigYml().getDouble("reforge.cost");
                                    if (status == ReforgeStatus.ALLOW) {
                                        ItemStack item = menu.getCaptiveItems(player).get(0);
                                        int reforges = ReforgeUtils.getReforges(item);
                                        cost *= Math.pow(plugin.getConfigYml().getDouble("reforge.cost-exponent"), reforges);
                                    }

                                    switch (status) {
                                        case INVALID_ITEM -> {
                                            previous.setType(Objects.requireNonNull(Material.getMaterial(plugin.getConfigYml().getString("gui.invalid-item.material").toUpperCase())));
                                            meta.setDisplayName(plugin.getConfigYml().getString("gui.invalid-item.name"));
                                            List<String> lore = new ArrayList<>();
                                            for (String string : plugin.getConfigYml().getStrings("gui.invalid-item.lore")) {
                                                lore.add(string.replace("%cost%", NumberUtils.format(cost)));
                                            }
                                            meta.setLore(lore);
                                        }
                                        case ALLOW -> {
                                            previous.setType(Objects.requireNonNull(Material.getMaterial(plugin.getConfigYml().getString("gui.allow.material").toUpperCase())));
                                            meta.setDisplayName(plugin.getConfigYml().getString("gui.allow.name"));
                                            List<String> lore = new ArrayList<>();
                                            for (String string : plugin.getConfigYml().getStrings("gui.allow.lore")) {
                                                lore.add(string.replace("%cost%", NumberUtils.format(cost)));
                                            }
                                            meta.setLore(lore);
                                        }
                                        case ALLOW_STONE -> {
                                            previous.setType(Objects.requireNonNull(Material.getMaterial(plugin.getConfigYml().getString("gui.allow-stone.material").toUpperCase())));
                                            meta.setDisplayName(plugin.getConfigYml().getString("gui.allow-stone.name"));
                                            List<String> lore = new ArrayList<>();
                                            for (String string : plugin.getConfigYml().getStrings("gui.allow-stone.lore")) {
                                                lore.add(string.replace("%cost%", NumberUtils.format(cost))
                                                        .replace("%stone%", ReforgeUtils.getReforgeStone(menu.getCaptiveItems(player).get(1)).getName()));
                                            }
                                            meta.setLore(lore);
                                        }
                                        default -> {
                                            previous.setType(Objects.requireNonNull(Material.getMaterial(plugin.getConfigYml().getString("gui.no-item.material").toUpperCase())));
                                            meta.setDisplayName(plugin.getConfigYml().getString("gui.no-item.name"));
                                            List<String> lore = new ArrayList<>();
                                            for (String string : plugin.getConfigYml().getStrings("gui.no-item.lore")) {
                                                lore.add(string.replace("%cost%", NumberUtils.format(cost)));
                                            }
                                            meta.setLore(lore);
                                        }
                                    }

                                    previous.setItemMeta(meta);
                                })
                                .onLeftClick((event, slot, menu) -> {
                                    Player player = (Player) event.getWhoClicked();
                                    ItemStack toReforge = menu.getCaptiveItems(player).isEmpty() ? null : menu.getCaptiveItems(player).get(0);
                                    if (toReforge == null) {
                                        return;
                                    }

                                    ReforgeTarget target = ReforgeTarget.getForMaterial(toReforge.getType());
                                    assert target != null;

                                    Reforge reforge = null;
                                    boolean usedStone = false;

                                    if (menu.getCaptiveItems(player).size() == 2) {
                                        Reforge stone = ReforgeUtils.getReforgeStone(menu.getCaptiveItems(player).get(1));
                                        if (stone != null) {
                                            if (stone.getTarget().getMaterials().contains(toReforge.getType())) {
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

                                    double cost = plugin.getConfigYml().getDouble("reforge.cost");
                                    int reforges = ReforgeUtils.getReforges(toReforge);
                                    cost *= Math.pow(plugin.getConfigYml().getDouble("reforge.cost-exponent"), reforges);

                                    if (!EconomyHandler.getInstance().has(player, cost)) {
                                        player.sendMessage(plugin.getLangYml().getMessage("insufficient-money"));

                                        player.playSound(
                                                player.getLocation(),
                                                Sound.valueOf(plugin.getConfigYml().getString("gui.insufficient-money-sound.id").toUpperCase()),
                                                1f,
                                                (float) plugin.getConfigYml().getDouble("gui.insufficient-money-sound.pitch")
                                        );

                                        return;
                                    }

                                    player.sendMessage(plugin.getLangYml().getMessage("applied-reforge").replace("%reforge%", reforge.getName()));

                                    ReforgeUtils.incrementReforges(toReforge);

                                    EconomyHandler.getInstance().withdrawPlayer(player, cost);

                                    ReforgeUtils.setReforge(toReforge, reforge);

                                    if (usedStone) {
                                        ItemStack stone = menu.getCaptiveItems(player).get(1);
                                        stone.setItemMeta(null);
                                        stone.setAmount(0);

                                        player.playSound(
                                                player.getLocation(),
                                                Sound.valueOf(plugin.getConfigYml().getString("gui.stone-sound.id").toUpperCase()),
                                                1f,
                                                (float) plugin.getConfigYml().getDouble("gui.stone-sound.pitch")
                                        );
                                    }

                                    player.playSound(
                                            player.getLocation(),
                                            Sound.valueOf(plugin.getConfigYml().getString("gui.sound.id").toUpperCase()),
                                            1f,
                                            (float) plugin.getConfigYml().getDouble("gui.sound.pitch")
                                    );
                                }).build()
                )
                .setSlot(6, 5,
                        Slot.builder(
                                new ItemStackBuilder(Material.BARRIER)
                                        .setDisplayName("&cClose")
                                        .build()
                        ).onLeftClick((event, slot) -> {
                            event.getWhoClicked().closeInventory();
                        }).build()
                )
                .build();
    }

    static {
        update(ReforgesPlugin.getInstance());
    }
}
