package com.willfp.reforges.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.PluginCommand
import org.bukkit.command.CommandSender

class CommandReforges(plugin: EcoPlugin) :
    PluginCommand(plugin, "reforges", "reforges.command.reforges", false) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        sender.sendMessage(
            plugin.langYml.getMessage("invalid-command")
        )
    }

    init {
        addSubcommand(CommandReload(plugin))
            .addSubcommand(CommandGive(plugin))
            .addSubcommand(CommandOpen(plugin))
            .addSubcommand(CommandApply(plugin))
    }
}
