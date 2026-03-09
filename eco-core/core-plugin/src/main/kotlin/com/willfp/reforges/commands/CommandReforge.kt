package com.willfp.reforges.commands

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.eco.core.sound.PlayableSound
import com.willfp.reforges.gui.ReforgeGUI
import com.willfp.reforges.plugin
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object CommandReforge : PluginCommand(
    plugin,
    "reforge",
    "reforges.command.reforge",
    true
) {
    @Suppress("DEPRECATION")
    override fun onExecute(player: CommandSender, args: List<String>) {
        player as Player
        PlayableSound.create(plugin.configYml.getSubsection("gui.open-sound"))?.playTo(player)
        ReforgeGUI.open(player)
    }
}
