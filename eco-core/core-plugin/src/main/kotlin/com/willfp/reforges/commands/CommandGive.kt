package com.willfp.reforges.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.reforges.reforges.Reforges
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.util.StringUtil

class CommandGive(
    plugin: EcoPlugin
) : Subcommand(plugin, "give", "reforges.command.give", false) {
    private val numbers = listOf(
        "1",
        "2",
        "3",
        "4",
        "5",
        "10",
        "32",
        "64"
    )

    override fun onExecute(sender: CommandSender, args: MutableList<String>) {
        if (args.isEmpty()) {
            sender.sendMessage(plugin.langYml.getMessage("needs-player"))
            return
        }

        if (args.size == 1) {
            sender.sendMessage(plugin.langYml.getMessage("needs-stone"))
            return
        }

        val amount = if (args.size > 2) args[2].toIntOrNull() ?: 1 else 1
        val recieverName = args[0]
        val reciever = Bukkit.getPlayer(recieverName)
        if (reciever == null) {
            sender.sendMessage(plugin.langYml.getMessage("invalid-player"))
            return
        }
        val key = args[1]
        val reforge = Reforges.getByKey(key)
        if (reforge == null) {
            sender.sendMessage(plugin.langYml.getMessage("invalid-stone"))
            return
        }
        var message = plugin.langYml.getMessage("give-success")
        message = message.replace("%reforge%", reforge.name).replace("%recipient%", reciever.name)
        sender.sendMessage(message)
        val itemStack = reforge.stone
        itemStack.amount = amount
        reciever.inventory.addItem(itemStack)
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {

        val completions = mutableListOf<String>()
        if (args.isEmpty()) {
            // Currently, this case is not ever reached
            return Reforges.values().filter { it.requiresStone }.map { it.id.key }
        }
        if (args.size == 1) {
            StringUtil.copyPartialMatches(
                args[0],
                Bukkit.getOnlinePlayers().map { it.name },
                completions
            )
            return completions
        }
        if (args.size == 2) {
            StringUtil.copyPartialMatches(
                args[1],
                Reforges.values().filter { it.requiresStone }.map { it.id.key },
                completions
            )
            completions.sort()
            return completions
        }
        if (args.size == 3) {
            StringUtil.copyPartialMatches(args[2], numbers, completions)
            completions.sortWith { s1, s2 ->
                val t1 = s1.toInt()
                val t2 = s2.toInt()
                t1 - t2
            }
            return completions
        }
        return emptyList()
    }
}