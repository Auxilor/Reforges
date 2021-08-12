package com.willfp.reforges.commands;


import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.CommandHandler;
import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.reforges.gui.ReforgeGUI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandReforge extends PluginCommand {
    /**
     * Instantiate a new command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandReforge(@NotNull final EcoPlugin plugin) {
        super(plugin, "reforge", "reforges.command.reforge", true);
    }

    @Override
    public CommandHandler getHandler() {
        return (sender, args) -> {
            ReforgeGUI.getMenu().open((Player) sender);
        };
    }
}
