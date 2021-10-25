package com.willfp.reforges.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.CommandHandler
import com.willfp.eco.core.command.TabCompleteHandler
import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.reforges.gui.ReforgeGUI.menu
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.util.StringUtil

class CommandOpen(
    plugin: EcoPlugin
) : PluginCommand(plugin, "open", "reforges.command.open", false) {
    override fun getHandler(): CommandHandler {
        return CommandHandler { sender, args ->
            if (args.isEmpty()) {
                sender.sendMessage(plugin.langYml.getMessage("needs-player"))
                return@CommandHandler
            }
            val player = Bukkit.getPlayer(args[0])
            if (player == null) {
                sender.sendMessage(plugin.langYml.getMessage("invalid-player"))
                return@CommandHandler
            }
            player.playSound(
                player.location,
                Sound.valueOf(plugin.configYml.getString("gui.open-sound.id").uppercase()),
                1f,
                plugin.configYml.getDouble("gui.open-sound.pitch").toFloat()
            )
            menu.open(player)
        }
    }

    override fun getTabCompleter(): TabCompleteHandler {
        return TabCompleteHandler { _, args ->
            val completions = mutableListOf<String>()
            if (args.size == 1) {
                StringUtil.copyPartialMatches(
                    args[0],
                    Bukkit.getOnlinePlayers().map { it.name },
                    completions
                )
                return@TabCompleteHandler completions
            }
            emptyList()
        }
    }
}