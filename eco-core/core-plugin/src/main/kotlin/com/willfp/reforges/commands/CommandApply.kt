package com.willfp.reforges.commands

import com.willfp.reforges.api.applyReforge
import com.willfp.reforges.reforges.Reforge
import com.willfp.reforges.reforges.Reforges
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import wiki.ecohub.config.emptyConfig
import wiki.ecohub.core.command.CommandBuilder
import wiki.ecohub.core.command.CommandContext
import wiki.ecohub.core.price.ConfiguredPrice
import wiki.ecohub.game.entity.runOwned

/**
 * A player applies to their own held item; the console names a player, and applies on their
 * region.
 */
internal fun CommandBuilder<CommandSender>.applyCommand() = subcommand(
    name = "apply",
    permission = "reforges.command.apply"
) {
    executes {
        val reforge = argument(
            index = 0,
            missingKey = "needs-reforge",
            invalidKey = "invalid-reforge"
        ) { Reforges.getByKey(it.lowercase()) }
        val self = sender as? Player

        if (self != null) {
            apply(self, reforge)
        } else {
            val player = argument(
                index = 1,
                missingKey = "needs-player",
                invalidKey = "invalid-player"
            ) { Bukkit.getPlayer(it) }
            player.runOwned { apply(player, reforge) }
        }
    }

    completes {
        when (args.size) {
            1 -> Reforges.values().map { it.id.key }
            2 -> playerNames()
            else -> emptyList()
        }
    }
}

private fun CommandContext<CommandSender>.apply(player: Player, reforge: Reforge) {
    val item = player.inventory.itemInMainHand

    val free = ConfiguredPrice.createOrFree(emptyConfig())

    when {
        item.type.isAir -> reply("no-item-in-hand")
        !reforge.canBeAppliedTo(item) -> reply("reforge-cannot-be-applied", "reforge" to reforge.name)
        !player.applyReforge(item, reforge, free) -> reply("reforge-application-cancelled")
        else -> reply("applied-reforge", "reforge" to reforge.name)
    }
}
