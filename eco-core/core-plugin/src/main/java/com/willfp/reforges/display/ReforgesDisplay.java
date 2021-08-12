package com.willfp.reforges.display;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.display.Display;
import com.willfp.eco.core.display.DisplayModule;
import com.willfp.eco.core.display.DisplayPriority;
import com.willfp.eco.core.fast.FastItemStack;
import com.willfp.eco.util.SkullUtils;
import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import com.willfp.reforges.reforges.util.ReforgeUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReforgesDisplay extends DisplayModule {
    /**
     * Create weapons display.
     *
     * @param plugin Instance of Reforges.
     */
    public ReforgesDisplay(@NotNull final EcoPlugin plugin) {
        super(plugin, DisplayPriority.HIGHEST);
    }

    @Override
    protected void display(@NotNull final ItemStack itemStack,
                           @NotNull final Object... args) {
        ReforgeTarget target = ReforgeTarget.getForMaterial(itemStack.getType());

        if (target == null) {
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;

        Reforge reforge = ReforgeUtils.getReforge(meta);
        Reforge stone = ReforgeUtils.getReforgeStone(meta);

        FastItemStack fastItemStack = FastItemStack.wrap(itemStack);
        List<String> lore = fastItemStack.getLore();
        assert lore != null;

        if (stone != null) {
            meta.setDisplayName(this.getPlugin().getConfigYml().getString("reforge.stone.name").replace("%reforge%", stone.getName()));
            SkullUtils.setSkullTexture((SkullMeta) meta, reforge.getConfig().getString("stone-config.texture"));
            itemStack.setItemMeta(meta);
            List<String> stoneLore = new ArrayList<>();
            for (String string : this.getPlugin().getConfigYml().getStrings("reforge.stone.lore")) {
                stoneLore.add(string.replace("%reforge%", stone.getName()));
            }
            lore.addAll(0, stoneLore);
        }

        if (reforge != null) {
            if (this.getPlugin().getConfigYml().getBool("reforge.display-in-lore")) {
                List<String> addLore = new ArrayList<>();

                addLore.add(" ");
                addLore.add(reforge.getName());

                List<String> description = new ArrayList<>(Arrays.asList(WordUtils.wrap(
                        reforge.getDescription(),
                        this.getPlugin().getConfigYml().getInt("reforge.line-wrap"),
                        "\n",
                        false
                ).split("\\r?\\n")));
                description.replaceAll(s -> this.getPlugin().getLangYml().getString("description-color") + s.replace("%description%", reforge.getDescription()));
                description.replaceAll(s -> s.replace("§r", this.getPlugin().getLangYml().getString("description-color")));
                addLore.addAll(description);

                addLore.replaceAll(s -> Display.PREFIX + s);
                lore.addAll(addLore);
            }
        }

        if (reforge == null && this.getPlugin().getConfigYml().getBool("reforge.show-reforgable")) {
            List<String> addLore = this.getPlugin().getConfigYml().getStrings("reforge.reforgable-suffix");

            addLore.replaceAll(s -> Display.PREFIX + s);
            lore.addAll(addLore);
        }

        fastItemStack.setLore(lore);
    }
}
