package com.willfp.reforges.commands;


import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.CommandHandler;
import com.willfp.eco.core.command.TabCompleteHandler;
import com.willfp.eco.core.command.impl.Subcommand;
import com.willfp.eco.core.config.updating.ConfigUpdater;
import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.Reforges;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandGive extends Subcommand {
    /**
     * The cached names.
     */
    private static final List<String> STONE_NAMES = new ArrayList<>();

    /**
     * The cached numbers.
     */
    private static final List<String> NUMBERS = Arrays.asList(
            "1",
            "2",
            "3",
            "4",
            "5",
            "10",
            "32",
            "64"
    );

    /**
     * Instantiate a new command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandGive(@NotNull final EcoPlugin plugin) {
        super(plugin, "give", "reforges.command.give", false);
    }

    /**
     * Called on reload.
     */
    @ConfigUpdater
    public static void reload() {
        STONE_NAMES.clear();
        STONE_NAMES.addAll(Reforges.values().stream()
                .filter(Reforge::getRequiresStone)
                .map(Reforge::getId)
                .collect(Collectors.toList()));
    }

    @Override
    public CommandHandler getHandler() {
        return (sender, args) -> {
            if (args.isEmpty()) {
                sender.sendMessage(this.getPlugin().getLangYml().getMessage("needs-player"));
                return;
            }

            if (args.size() == 1) {
                sender.sendMessage(this.getPlugin().getLangYml().getMessage("needs-stone"));
                return;
            }

            int amount = 1;

            if (args.size() > 2) {
                try {
                    amount = Integer.parseInt(args.get(2));
                } catch (NumberFormatException ignored) {
                    // do nothing
                }
            }

            String recieverName = args.get(0);
            Player reciever = Bukkit.getPlayer(recieverName);

            if (reciever == null) {
                sender.sendMessage(this.getPlugin().getLangYml().getMessage("invalid-player"));
                return;
            }

            String key = args.get(1);

            Reforge reforge = Reforges.getByKey(key);

            if (reforge == null) {
                sender.sendMessage(this.getPlugin().getLangYml().getMessage("invalid-stone"));
                return;
            }

            String message = this.getPlugin().getLangYml().getMessage("give-success");
            message = message.replace("%reforge%", reforge.getName()).replace("%recipient%", reciever.getName());
            sender.sendMessage(message);

            ItemStack itemStack = reforge.getStone();
            itemStack.setAmount(amount);
            reciever.getInventory().addItem(itemStack);
        };
    }

    @Override
    public TabCompleteHandler getTabCompleter() {
        return (sender, args) -> {
            List<String> completions = new ArrayList<>();

            if (args.isEmpty()) {
                // Currently, this case is not ever reached
                return STONE_NAMES;
            }

            if (args.size() == 1) {
                StringUtil.copyPartialMatches(args.get(0), Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()), completions);
                return completions;
            }

            if (args.size() == 2) {
                StringUtil.copyPartialMatches(args.get(1), STONE_NAMES, completions);

                Collections.sort(completions);
                return completions;
            }

            if (args.size() == 3) {
                StringUtil.copyPartialMatches(args.get(2), NUMBERS, completions);

                completions.sort((s1, s2) -> {
                    int t1 = Integer.parseInt(s1);
                    int t2 = Integer.parseInt(s2);
                    return t1 - t2;
                });

                return completions;
            }

            return new ArrayList<>(0);
        };
    }
}
