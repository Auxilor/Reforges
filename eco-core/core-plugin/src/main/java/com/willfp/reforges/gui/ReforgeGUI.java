package com.willfp.reforges.gui;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.updating.ConfigUpdater;
import com.willfp.eco.core.drops.DropQueue;
import com.willfp.eco.core.gui.menu.Menu;
import com.willfp.eco.core.gui.slot.FillerMask;
import com.willfp.eco.core.gui.slot.MaskMaterials;
import com.willfp.eco.core.gui.slot.Slot;
import com.willfp.eco.core.items.builder.ItemStackBuilder;
import com.willfp.eco.util.NumberUtils;
import com.willfp.reforges.ReforgesPlugin;
import com.willfp.reforges.reforges.util.ReforgeHandler;
import com.willfp.reforges.reforges.util.ReforgeStatus;
import com.willfp.reforges.reforges.util.ReforgeUtils;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecation")
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
    @ConfigUpdater
    public static void update(@NotNull final EcoPlugin plugin) {
        ReforgeHandler handler = new ReforgeHandler(plugin);
        Slot activatorSlot = Slot.builder(new ItemStack(Material.ANVIL))
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

                    int xpcost = plugin.getConfigYml().getInt("reforge.xp-cost");
                    if (status == ReforgeStatus.ALLOW) {
                        ItemStack item = menu.getCaptiveItems(player).get(0);
                        int reforges = ReforgeUtils.getReforges(item);
                        xpcost *= Math.pow(plugin.getConfigYml().getDouble("reforge.cost-exponent"), reforges);
                    }

                    switch (status) {
                        case INVALID_ITEM -> {
                            previous.setType(Objects.requireNonNull(Material.getMaterial(plugin.getConfigYml().getString("gui.invalid-item.material").toUpperCase())));
                            meta.setDisplayName(plugin.getConfigYml().getString("gui.invalid-item.name"));
                            List<String> lore = new ArrayList<>();
                            for (String string : plugin.getConfigYml().getStrings("gui.invalid-item.lore")) {
                                lore.add(string.replace("%cost%", NumberUtils.format(cost))
                                .replace("%xpcost%", NumberUtils.format(xpcost)));
                            }
                            meta.setLore(lore);
                        }
                        case ALLOW -> {
                            previous.setType(Objects.requireNonNull(Material.getMaterial(plugin.getConfigYml().getString("gui.allow.material").toUpperCase())));
                            meta.setDisplayName(plugin.getConfigYml().getString("gui.allow.name"));
                            List<String> lore = new ArrayList<>();
                            for (String string : plugin.getConfigYml().getStrings("gui.allow.lore")) {
                                lore.add(string.replace("%cost%", NumberUtils.format(cost))
                                        .replace("%xpcost%", NumberUtils.format(xpcost)));
                            }
                            meta.setLore(lore);
                        }
                        case ALLOW_STONE -> {
                            previous.setType(Objects.requireNonNull(Material.getMaterial(plugin.getConfigYml().getString("gui.allow-stone.material").toUpperCase())));
                            meta.setDisplayName(plugin.getConfigYml().getString("gui.allow-stone.name"));
                            List<String> lore = new ArrayList<>();
                            for (String string : plugin.getConfigYml().getStrings("gui.allow-stone.lore")) {
                                lore.add(string.replace("%cost%", NumberUtils.format(cost))
                                        .replace("%xpcost%", NumberUtils.format(xpcost))
                                        .replace("%stone%", ReforgeUtils.getReforgeStone(menu.getCaptiveItems(player).get(1)).getName()));
                            }
                            meta.setLore(lore);
                        }
                        default -> {
                            previous.setType(Objects.requireNonNull(Material.getMaterial(plugin.getConfigYml().getString("gui.no-item.material").toUpperCase())));
                            meta.setDisplayName(plugin.getConfigYml().getString("gui.no-item.name"));
                            List<String> lore = new ArrayList<>();
                            for (String string : plugin.getConfigYml().getStrings("gui.no-item.lore")) {
                                lore.add(string.replace("%cost%", NumberUtils.format(cost))
                                        .replace("%xpcost%", NumberUtils.format(xpcost)));
                            }
                            meta.setLore(lore);
                        }
                    }

                    previous.setItemMeta(meta);
                })
                .onLeftClick(handler::handleReforgeClick)
                .build();

        String[] maskPattern = plugin.getConfigYml().getStrings("gui.mask.pattern", false).toArray(new String[0]);
        Material[] maskMaterials = plugin.getConfigYml()
                .getStrings("gui.mask.materials", false)
                .stream()
                .map(string -> Material.getMaterial(string.toUpperCase()))
                .filter(Objects::nonNull)
                .toArray(Material[]::new);

        Material allowMaterial = Material.getMaterial(plugin.getConfigYml().getString("gui.show-allowed.allow-material", false).toUpperCase());
        Material denyMaterial = Material.getMaterial(plugin.getConfigYml().getString("gui.show-allowed.deny-material", false).toUpperCase());
        assert allowMaterial != null;
        assert denyMaterial != null;

        Material closeMaterial = Material.getMaterial(plugin.getConfigYml().getString("gui.close.material", false).toUpperCase());
        assert closeMaterial != null;

        menu = Menu.builder(plugin.getConfigYml().getInt("gui.rows"))
                .setTitle(plugin.getLangYml().getString("menu.title"))
                .setMask(
                        new FillerMask(
                                new MaskMaterials(
                                        maskMaterials
                                ),
                                maskPattern
                        )
                )
                .modfiy(builder -> {
                    Slot slot = Slot.builder(
                            new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE)
                                    .setDisplayName("&r")
                                    .build()
                    ).setModifier((player, menu, previous) -> {
                        ReforgeStatus status = ReforgeUtils.getStatus(menu.getCaptiveItems(player));
                        if (status == ReforgeStatus.ALLOW || status == ReforgeStatus.ALLOW_STONE) {
                            previous.setType(allowMaterial);
                        } else {
                            previous.setType(denyMaterial);
                        }
                    }).build();

                    List<String> allowedPattern = plugin.getConfigYml().getStrings("gui.show-allowed.pattern");

                    for (int i = 1; i <= allowedPattern.size(); i++) {
                        String row = allowedPattern.get(i - 1);
                        for (int j = 1; j <= 9; j++) {
                            if (row.charAt(j - 1) != '0') {
                                builder.setSlot(i, j, slot);
                            }
                        }
                    }
                })
                .setSlot(plugin.getConfigYml().getInt("gui.item-slot.row"),
                        plugin.getConfigYml().getInt("gui.item-slot.column"),
                        Slot.builder()
                                .setCaptive()
                                .build()
                )
                .setSlot(plugin.getConfigYml().getInt("gui.stone-slot.row"),
                        plugin.getConfigYml().getInt("gui.stone-slot.column"),
                        Slot.builder()
                                .setCaptive()
                                .build()
                )
                .setSlot(2, 5, activatorSlot)
                .setSlot(plugin.getConfigYml().getInt("gui.close.location.row"),
                        plugin.getConfigYml().getInt("gui.close.location.column"),
                        Slot.builder(
                                new ItemStackBuilder(closeMaterial)
                                        .setDisplayName(plugin.getLangYml().getString("menu.close"))
                                        .build()
                        ).onLeftClick((event, slot, menu) -> {
                            event.getWhoClicked().closeInventory();
                        }).build()
                ).onClose((event, menu) -> {
                    new DropQueue((Player) event.getPlayer())
                            .addItems(menu.getCaptiveItems((Player) event.getPlayer()))
                            .setLocation(event.getPlayer().getEyeLocation())
                            .push();
                })
                .build();
    }

    static {
        update(ReforgesPlugin.getInstance());
    }
}
