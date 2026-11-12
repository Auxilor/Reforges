package com.willfp.reforges.commands

import com.willfp.reforges.reforges.Reforges
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import wiki.ecohub.core.command.CommandBuilder
import wiki.ecohub.game.drop.DropQueue
import wiki.ecohub.game.entity.runOwned

private val amountCompletions = listOf("1", "2", "3", "4", "5", "10", "32", "64")

private const val AMOUNT_ARGS = 3

private const val MAX_STACKS = 36

/**
 * Gives on the receiver's region, as the console runs off it on Folia.
 */
internal fun CommandBuilder<CommandSender>.giveCommand() = subcommand(
    name = "give",
    permission = "reforges.command.give"
) {
    executes {
        val receiverName = argument(0, "needs-player")
        val key = argument(1, "needs-stone")

        val receiver = Bukkit.getPlayer(receiverName) ?: fail("invalid-player")
        val reforge = Reforges.getByKey(key) ?: fail("invalid-stone")

        val stone = reforge.stone
        val amount = (args.getOrNull(2)?.toIntOrNull() ?: 1)
            .coerceIn(minimumValue = 1, maximumValue = stone.maxStackSize * MAX_STACKS)

        receiver.runOwned {
            reply("give-success", "reforge" to reforge.name, "recipient" to receiver.name)

            DropQueue(receiver)
                .addItems(
                    (amount downTo 1 step stone.maxStackSize).map { remaining ->
                        stone.clone().apply { this.amount = minOf(a = remaining, b = stone.maxStackSize) }
                    }
                )
                .forceTelekinesis()
                .push()
        }
    }

    completes {
        when (args.size) {
            1 -> playerNames()
            2 -> Reforges.values().filter { it.requiresStone }.map { it.id.key }
            AMOUNT_ARGS -> amountCompletions
            else -> emptyList()
        }
    }
}
