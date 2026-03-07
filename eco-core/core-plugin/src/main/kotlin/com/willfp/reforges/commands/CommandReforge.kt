package com.willfp.reforges.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.eco.core.sound.PlayableSound
import com.willfp.reforges.gui.ReforgeGUI
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandReforge(
    plugin: EcoPlugin
) : PluginCommand(plugin, "reforge", "reforges.command.reforge", true) {
    override fun onExecute(player: CommandSender, args: List<String>) {
        player as Player
        PlayableSound.create(plugin.configYml.getSubsection("gui.open-sound"))?.playTo(player)
        ReforgeGUI.open(player)
    }
}
