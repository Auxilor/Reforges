package com.willfp.reforges.commands

import com.willfp.reforges.gui.ReforgeGUI
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import wiki.ecohub.core.command.CommandBuilder
import wiki.ecohub.game.entity.runOwned
import wiki.ecohub.game.sound.PlayableSound

/**
 * Opens on the target's region, as the sender runs off it on Folia.
 */
internal fun CommandBuilder<CommandSender>.openCommand() = subcommand(
    name = "open",
    permission = "reforges.command.open"
) {
    executes {
        val player = argument(
            index = 0,
            missingKey = "needs-player",
            invalidKey = "invalid-player"
        ) { Bukkit.getPlayer(it) }

        player.runOwned {
            PlayableSound.create(plugin.configYml.getSubsection("gui.open-sound"))?.playTo(player)
            ReforgeGUI.open(player)
        }
    }

    completes {
        if (args.size == 1) playerNames() else emptyList()
    }
}
