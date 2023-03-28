package com.willfp.reforges

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.integrations.IntegrationLoader
import com.willfp.eco.core.items.Items
import com.willfp.libreforge.conditions.Conditions
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.loader.configs.ConfigCategory
import com.willfp.libreforge.registerHolderProvider
import com.willfp.libreforge.registerPlayerRefreshFunction
import com.willfp.reforges.commands.CommandReforge
import com.willfp.reforges.commands.CommandReforges
import com.willfp.reforges.config.TargetYml
import com.willfp.reforges.display.ReforgesDisplay
import com.willfp.reforges.integrations.talismans.TalismansIntegration
import com.willfp.reforges.libreforge.ConditionHasReforge
import com.willfp.reforges.reforges.Reforges
import com.willfp.reforges.reforges.util.ReforgeArgParser
import com.willfp.reforges.util.AntiPlaceListener
import com.willfp.reforges.util.DiscoverRecipeListener
import com.willfp.reforges.util.ReforgeLookup
import org.bukkit.event.Listener

class ReforgesPlugin : LibreforgePlugin() {
    val targetYml: TargetYml =
        TargetYml(this)

    init {
        instance = this
    }

    override fun loadConfigCategories(): List<ConfigCategory> {
        return listOf(
            Reforges
        )
    }

    override fun handleEnable() {
        Conditions.register(ConditionHasReforge)

        Items.registerArgParser(ReforgeArgParser)

        registerHolderProvider { ReforgeLookup.provideReforges(it) }
        registerPlayerRefreshFunction { ReforgeLookup.clearCache(it) }
    }

    override fun loadListeners(): List<Listener> {
        return listOf(
            DiscoverRecipeListener(this),
            AntiPlaceListener(),
        )
    }

    override fun loadPluginCommands(): List<PluginCommand> {
        return listOf(
            CommandReforge(this),
            CommandReforges(this)
        )
    }

    override fun createDisplayModule(): DisplayModule {
        return ReforgesDisplay(this)
    }

    override fun loadIntegrationLoaders(): List<IntegrationLoader> {
        return listOf(
            IntegrationLoader("Talismans") { TalismansIntegration.registerProvider() }
        )
    }

    companion object {
        /**
         * Instance of Reforges.
         */
        @JvmStatic
        lateinit var instance: ReforgesPlugin
            private set
    }
}
