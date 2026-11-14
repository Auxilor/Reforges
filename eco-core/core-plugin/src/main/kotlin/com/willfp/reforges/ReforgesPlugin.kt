package com.willfp.reforges

import com.willfp.reforges.commands.reforgeCommand
import com.willfp.reforges.commands.reforgesCommand
import com.willfp.reforges.config.TargetYml
import com.willfp.reforges.display.ReforgesDisplay
import com.willfp.reforges.gui.ReforgeGUI
import com.willfp.reforges.libreforge.ConditionHasReforge
import com.willfp.reforges.libreforge.EffectApplyRandomReforge
import com.willfp.reforges.libreforge.EffectApplyReforge
import com.willfp.reforges.libreforge.EffectRemoveReforge
import com.willfp.reforges.libreforge.FilterReforge
import com.willfp.reforges.libreforge.TriggerReforgeItem
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
import wiki.ecohub.config.category.NativeConfigCategory
import wiki.ecohub.core.command.EcoCommand
import wiki.ecohub.core.plugin.EcoPlugin
import wiki.ecohub.game.item.Items
import wiki.ecohub.provider.metrics.EcoMetricsChart
import wiki.ecohub.rules.condition.Conditions
import wiki.ecohub.rules.effect.Effects
import wiki.ecohub.rules.filter.Filters
import wiki.ecohub.rules.holder.registerHolderProvider
import wiki.ecohub.rules.trigger.Triggers
import wiki.ecohub.ui.display.DisplayModule

internal lateinit var plugin: ReforgesPlugin
    private set

class ReforgesPlugin : EcoPlugin() {
    val targetYml: TargetYml = TargetYml(this)

    init {
        plugin = this
    }

    override fun loadConfigCategories(): List<NativeConfigCategory> {
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

        Effects.register(EffectApplyReforge)
        Effects.register(EffectApplyRandomReforge)
        Effects.register(EffectRemoveReforge)
        Triggers.register(TriggerReforgeItem)
        Filters.register(FilterReforge)
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

    override fun loadPluginCommands(): List<EcoCommand> {
        return listOf(
            reforgeCommand(),
            reforgesCommand()
        )
    }

    override fun loadDisplayModules(): List<DisplayModule> {
        return listOf(
            ReforgesDisplay
        )
    }

    override fun getCustomCharts() = listOf(
        EcoMetricsChart.SingleLine("total_reforges") { Reforges.values().size },
        EcoMetricsChart.SingleLine("total_reforge_targets") { ReforgeTargets.values().size },
        EcoMetricsChart.SingleLine("total_price_multipliers") { PriceMultipliers.values().size },
        EcoMetricsChart.SimplePie("discover_recipes") {
            if (configYml.getBool("discover-recipes")) "enabled" else "disabled"
        },
        EcoMetricsChart.SimplePie("no_offhand") {
            if (configYml.getBool("no-offhand")) "enabled" else "disabled"
        },
        EcoMetricsChart.SimplePie("display_in_lore") {
            if (configYml.getBool("reforge.display-in-lore")) "enabled" else "disabled"
        },
        EcoMetricsChart.SimplePie("display_in_name") {
            if (configYml.getBool("reforge.display-in-name")) "enabled" else "disabled"
        }
    )
}
