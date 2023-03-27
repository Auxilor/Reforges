package com.willfp.reforges.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.reforges.reforges.Reforges
import com.willfp.reforges.util.reforge
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

class CommandApply(
    plugin: EcoPlugin
) : Subcommand(plugin, "apply", "reforges.command.apply", false) {
    override fun onExecute(sender: CommandSender, args: MutableList<String>) {
        if (args.isEmpty()) {
            sender.sendMessage(plugin.langYml.getMessage("needs-reforge"))
            return
        }

        val reforge = Reforges.getByKey(args[0].lowercase())

        if (reforge == null) {
            sender.sendMessage(plugin.langYml.getMessage("invalid-reforge"))
            return
        }

        if (sender is Player) {
            val item = sender.inventory.itemInMainHand
            item.reforge = reforge
            sender.sendMessage(
                plugin.langYml.getMessage("applied-reforge")
                    .replace("%reforge%", reforge.name)
            )
        } else {
            if (args.size < 2) {
                sender.sendMessage(plugin.langYml.getMessage("needs-player"))
                return
            }

            val player = Bukkit.getPlayer(args[1])

            if (player == null) {
                sender.sendMessage(plugin.langYml.getMessage("invalid-player"))
                return
            }

            player.inventory.itemInMainHand.reforge = reforge
            sender.sendMessage(
                plugin.langYml.getMessage("applied-reforge")
                    .replace("%reforge%", reforge.name)
            )
        }
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        val completions = mutableListOf<String>()
        if (args.isEmpty()) {
            // Currently, this case is not ever reached
            return Reforges.values().map { it.id.key }
        }
        if (args.size == 1) {
            StringUtil.copyPartialMatches(
                args[0],
                Reforges.values().map { it.id.key },
                completions
            )
            completions.sort()
            return completions
        }
        if (args.size == 2) {
            StringUtil.copyPartialMatches(
                args[1],
                Bukkit.getOnlinePlayers().map { it.name },
                completions
            )
            completions.sort()
            return completions
        }
        return emptyList()
    }
}