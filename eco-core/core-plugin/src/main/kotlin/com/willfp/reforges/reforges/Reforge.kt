package com.willfp.reforges.reforges

import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.eco.core.display.Display
import com.willfp.eco.core.items.CustomItem
import com.willfp.eco.core.items.builder.SkullBuilder
import com.willfp.eco.core.recipe.Recipes
import com.willfp.reforges.ReforgesPlugin
import com.willfp.reforges.conditions.Conditions
import com.willfp.reforges.conditions.ConfiguredCondition
import com.willfp.reforges.effects.ConfiguredEffect
import com.willfp.reforges.effects.Effects
import com.willfp.reforges.reforges.meta.ReforgeTarget
import com.willfp.reforges.reforges.util.ReforgeUtils
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

@Suppress("DEPRECATION")
class Reforge(
    internal val config: JSONConfig,
    private val plugin: ReforgesPlugin
) {
    val id = config.getString("id")

    val name = config.getString("name")

    val description: List<String> = config.getStrings("description")

    val targets = config.getStrings("targets").map { ReforgeTarget.getByName(it) }.toSet()

    val effects = config.getSubsections("effects").map {
        val effect = Effects.getByID(it.getString("id")) ?: return@map null
        ConfiguredEffect(effect, it)
    }.filterNotNull().toSet()

    val conditions = config.getSubsections("conditions").map {
        val condition = Conditions.getByID(it.getString("id")) ?: return@map null
        ConfiguredCondition(condition, it)
    }.filterNotNull().toSet()

    val requiresStone = config.getBool("stone.enabled")

    val stone: ItemStack = SkullBuilder().apply {
        if (config.getBool("stone.enabled")) {
            setSkullTexture(config.getString("stone.texture"))
            setDisplayName(plugin.configYml.getString("stone.name").replace("%reforge%", name))
            addLoreLines(
                plugin.configYml.getStrings("stone.lore").map { "${Display.PREFIX}${it.replace("%reforge%", name)}" })
        }
    }.build()

    init {
        ReforgeUtils.setReforgeStone(stone, this)

        CustomItem(
            plugin.namespacedKeyFactory.create("stone_" + this.id),
            { test -> ReforgeUtils.getReforgeStone(test) == this },
            stone
        ).register()

        if (config.getBool("stone.craftable") && config.getBool("stone.enabled")) {
            Recipes.createAndRegisterRecipe(
                plugin,
                "stone_" + this.id,
                stone,
                config.getStrings("stone.recipe", false)
            )
        }
    }

    fun handleActivation(player: Player) {
        for ((effect, config) in this.effects) {
            effect.handleEnabling(player, config)
        }
    }

    fun handleDeactivation(player: Player) {
        for ((effect, _) in this.effects) {
            effect.handleDisabling(player)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other !is Reforge) {
            return false
        }

        return other.id == this.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun toString(): String {
        return "Reforge{$id}"
    }
}