package com.willfp.reforges.commands;


import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.CommandHandler;
import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.reforges.gui.ReforgeGUI;
import org.bukkit.Sound;
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
            Player player = (Player) sender;

            player.playSound(
                    player.getLocation(),
                    Sound.valueOf(this.getPlugin().getConfigYml().getString("gui.open-sound.id").toUpperCase()),
                    1f,
                    (float) this.getPlugin().getConfigYml().getDouble("gui.open-sound.pitch")
            );
            ReforgeGUI.getMenu().open(player);
        };
    }
}
