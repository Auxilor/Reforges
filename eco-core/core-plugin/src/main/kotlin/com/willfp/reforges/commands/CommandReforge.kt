package com.willfp.reforges.commands

import com.willfp.reforges.ReforgesPlugin
import com.willfp.reforges.gui.ReforgeGUI
import wiki.ecohub.core.command.playerCommand
import wiki.ecohub.game.sound.PlayableSound

internal fun ReforgesPlugin.reforgeCommand() = playerCommand(
    name = "reforge",
    permission = "reforges.command.reforge"
) {
    executes {
        PlayableSound.create(plugin.configYml.getSubsection("gui.open-sound"))?.playTo(sender)
        ReforgeGUI.open(sender)
    }
}
