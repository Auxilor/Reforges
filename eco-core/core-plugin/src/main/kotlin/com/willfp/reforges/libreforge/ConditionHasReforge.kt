package com.willfp.reforges.libreforge

import com.willfp.reforges.reforges.Reforge
import org.bukkit.entity.Player
import wiki.ecohub.config.ArgType
import wiki.ecohub.config.Config
import wiki.ecohub.config.arguments
import wiki.ecohub.core.registry.NoCompileData
import wiki.ecohub.game.entity.toDispatcher
import wiki.ecohub.rules.condition.Condition
import wiki.ecohub.rules.holder.Dispatcher
import wiki.ecohub.rules.holder.ProvidedHolder
import wiki.ecohub.rules.holder.get
import wiki.ecohub.rules.holder.holders
import wiki.ecohub.ui.text.containsIgnoreCase

object ConditionHasReforge : Condition<NoCompileData>("has_reforge") {
    override val description = "Passes when the player currently has the specified reforge applied to any of their items."

    override val categories = setOf("inventory")

    override val arguments = arguments {
        require(
            "reforge",
            "You must specify the reforge!",
            description = "The id of the reforge to check for.",
            type = ArgType.STRING,
            example = "sharp"
        )
    }

    override fun isMet(
        dispatcher: Dispatcher<*>,
        config: Config,
        holder: ProvidedHolder,
        compileData: NoCompileData
    ): Boolean {
        val player = dispatcher.get<Player>() ?: return false

        return player.toDispatcher().holders.mapNotNull { it.holder as? Reforge }
            .map { it.id.key }
            .containsIgnoreCase(config.getString("reforge"))
    }
}
