package com.willfp.reforges.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.CommandHandler
import com.willfp.eco.core.command.impl.Subcommand

class CommandReload(plugin: EcoPlugin) : Subcommand(plugin, "reload", "reforges.command.reload", false) {
    override fun getHandler(): CommandHandler {
        return CommandHandler { sender, _ ->
            plugin.reload()
            sender.sendMessage(plugin.langYml.getMessage("reloaded"))
        }
    }
}