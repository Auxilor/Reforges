package com.willfp.reforges.commands;


import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.CommandHandler;
import com.willfp.eco.core.command.TabCompleteHandler;
import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.reforges.gui.ReforgeGUI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandOpen extends PluginCommand {
    /**
     * Instantiate a new command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandOpen(@NotNull final EcoPlugin plugin) {
        super(plugin, "open", "reforges.command.open", false);
    }

    @Override
    public CommandHandler getHandler() {
        return (sender, args) -> {
            if (args.isEmpty()) {
                sender.sendMessage(this.getPlugin().getLangYml().getMessage("needs-player"));
                return;
            }

            Player player = Bukkit.getPlayer(args.get(0));

            if (player == null) {
                sender.sendMessage(this.getPlugin().getLangYml().getMessage("invalid-player"));
                return;
            }

            player.playSound(
                    player.getLocation(),
                    Sound.valueOf(this.getPlugin().getConfigYml().getString("gui.open-sound.id").toUpperCase()),
                    1f,
                    (float) this.getPlugin().getConfigYml().getDouble("gui.open-sound.pitch")
            );
            ReforgeGUI.getMenu().open(player);
        };
    }

    @Override
    public TabCompleteHandler getTabCompleter() {
        return (sender, args) -> {
            List<String> completions = new ArrayList<>();

            if (args.size() == 1) {
                StringUtil.copyPartialMatches(args.get(0), Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()), completions);
                return completions;
            }

            return new ArrayList<>(0);
        };
    }
}
