package com.willfp.reforges.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.CommandHandler
import com.willfp.eco.core.command.impl.PluginCommand

class CommandReforges(plugin: EcoPlugin) : PluginCommand(plugin, "reforges", "reforges.command.reforges", false) {
    override fun getHandler(): CommandHandler {
        return CommandHandler { sender, _ ->
            sender.sendMessage(
                plugin.langYml.getMessage("invalid-command")
            )
        }
    }

    init {
        addSubcommand(CommandReload(plugin))
            .addSubcommand(CommandGive(plugin))
            .addSubcommand(CommandOpen(plugin))
    }
}