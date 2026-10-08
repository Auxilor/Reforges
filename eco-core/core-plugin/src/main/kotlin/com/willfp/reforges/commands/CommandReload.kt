package com.willfp.reforges.commands

import com.willfp.eco.core.Prerequisite
import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.eco.util.StringUtils
import com.willfp.eco.util.toNiceString
import com.willfp.reforges.plugin
import com.willfp.reforges.reforges.Reforges
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

object CommandReload : Subcommand(
    plugin,
    "reload",
    "reforges.command.reload",
    false
) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        // Reloading rebuilds shared registries, so it belongs on the global region.
        if (Prerequisite.HAS_FOLIA.isMet && !Bukkit.isGlobalTickThread()) {
            plugin.scheduler.global().run { onExecute(sender, args) }
            return
        }

        sender.sendMessage(
            plugin.langYml.getMessage("reloaded", StringUtils.FormatOption.WITHOUT_PLACEHOLDERS)
                .replace("%time%", plugin.reloadWithTime().toNiceString())
                .replace("%count%", Reforges.values().size.toNiceString())
        )
    }
}