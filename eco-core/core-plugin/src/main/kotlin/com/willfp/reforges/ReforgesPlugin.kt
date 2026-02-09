package com.willfp.reforges

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.items.Items
import com.willfp.libreforge.conditions.Conditions
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.loader.configs.ConfigCategory
import com.willfp.libreforge.registerHolderProvider
import com.willfp.reforges.commands.CommandReforge
import com.willfp.reforges.commands.CommandReforges
import com.willfp.reforges.config.TargetYml
import com.willfp.reforges.display.ReforgesDisplay
import com.willfp.reforges.gui.ReforgeGUI
import com.willfp.reforges.libreforge.ConditionHasReforge
import com.willfp.reforges.reforges.PriceMultipliers
import com.willfp.reforges.reforges.ReforgeFinder
import com.willfp.reforges.reforges.ReforgeStoneTag
import com.willfp.reforges.reforges.ReforgeTargets
import com.willfp.reforges.reforges.ReforgedTag
import com.willfp.reforges.reforges.Reforges
import com.willfp.reforges.reforges.util.ReforgeArgParser
import com.willfp.reforges.util.AntiPlaceListener
import com.willfp.reforges.util.DiscoverRecipeListener
import org.bukkit.event.Listener

internal lateinit var plugin: ReforgesPlugin
    private set

class ReforgesPlugin : LibreforgePlugin() {
    val targetYml: TargetYml = TargetYml

    init {
        plugin = this
    }

    override fun loadConfigCategories(): List<ConfigCategory> {
        return listOf(
            Reforges
        )
    }

    override fun handleEnable() {
        Conditions.register(ConditionHasReforge)

        Items.registerArgParser(ReforgeArgParser)

        Items.registerTag(ReforgedTag)
        Items.registerTag(ReforgeStoneTag)

        registerHolderProvider(ReforgeFinder.toHolderProvider())
    }

    override fun handleReload() {
        ReforgeTargets.update()
        PriceMultipliers.update()
        ReforgeGUI.update()
    }

    override fun loadListeners(): List<Listener> {
        return listOf(
            DiscoverRecipeListener,
            AntiPlaceListener,
        )
    }

    override fun loadPluginCommands(): List<PluginCommand> {
        return listOf(
            CommandReforge,
            CommandReforges
        )
    }

    override fun loadDisplayModules(): List<DisplayModule> {
        return listOf(
            ReforgesDisplay
        )
    }
}
