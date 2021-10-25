package com.willfp.reforges.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.CommandHandler
import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.reforges.gui.ReforgeGUI.menu
import org.bukkit.Sound
import org.bukkit.entity.Player

class CommandReforge(
    plugin: EcoPlugin
) : PluginCommand(plugin, "reforge", "reforges.command.reforge", true) {
    override fun getHandler(): CommandHandler {
        return CommandHandler { player, _ ->
            player as Player
            player.playSound(
                player.location,
                Sound.valueOf(plugin.configYml.getString("gui.open-sound.id").uppercase()),
                1f,
                plugin.configYml.getDouble("gui.open-sound.pitch").toFloat()
            )
            menu.open(player)
        }
    }
}