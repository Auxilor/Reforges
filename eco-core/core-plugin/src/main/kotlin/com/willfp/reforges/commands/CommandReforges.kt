package com.willfp.reforges.commands

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.reforges.plugin
import org.bukkit.command.CommandSender

object CommandReforges: PluginCommand(
    plugin,
    "reforges",
    "reforges.command.reforges",
    false
) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        sender.sendMessage(
            plugin.langYml.getMessage("invalid-command")
        )
    }

    init {
        addSubcommand(CommandReload)
            .addSubcommand(CommandGive)
            .addSubcommand(CommandOpen)
            .addSubcommand(CommandApply)
    }
}
