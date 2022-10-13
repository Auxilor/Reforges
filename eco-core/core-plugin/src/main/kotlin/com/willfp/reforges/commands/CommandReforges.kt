package com.willfp.reforges.commands

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.libreforge.LibReforgePlugin
import com.willfp.libreforge.lrcdb.CommandExport
import com.willfp.libreforge.lrcdb.CommandImport
import com.willfp.libreforge.lrcdb.ExportableConfig
import com.willfp.reforges.reforges.Reforges
import org.bukkit.command.CommandSender

class CommandReforges(plugin: LibReforgePlugin) :
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
            .addSubcommand(CommandImport("reforges", plugin))
            .addSubcommand(CommandExport(plugin) {
                Reforges.values().map {
                    ExportableConfig(
                        it.id,
                        it.config
                    )
                }
            })
    }
}
