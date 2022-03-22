package com.willfp.reforges.reforges

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.display.Display
import com.willfp.eco.core.items.CustomItem
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.builder.ItemStackBuilder
import com.willfp.eco.core.recipe.Recipes
import com.willfp.libreforge.Holder
import com.willfp.libreforge.conditions.Conditions
import com.willfp.libreforge.effects.Effects
import com.willfp.reforges.ReforgesPlugin
import com.willfp.reforges.reforges.meta.ReforgeTarget
import com.willfp.reforges.reforges.util.ReforgeUtils
import org.bukkit.inventory.ItemStack
import java.util.Objects

@Suppress("DEPRECATION")
class Reforge(
    internal val config: Config,
    plugin: ReforgesPlugin
) : Holder {
    val id = config.getString("id")

    val name = config.getFormattedString("name")

    val description: List<String> = config.getFormattedStrings("description")

    val targets = config.getStrings("targets").map { ReforgeTarget.getByName(it) }.toSet()

    override val effects = config.getSubsections("effects").mapNotNull {
        Effects.compile(it, "Reforge ID $id")
    }.toSet()

    override val conditions = config.getSubsections("conditions").mapNotNull {
        Conditions.compile(it, "Reforge ID $id")
    }.toSet()

    val requiresStone = config.getBool("stone.enabled")

    val stone: ItemStack = ItemStackBuilder(Items.lookup(config.getString("stone.item")).item).apply {
        if (config.getBool("stone.enabled")) {
            setDisplayName(config.getFormattedString("stone.name").replace("%reforge%", name))
            addLoreLines(
                config.getFormattedStrings("stone.lore").map { "${Display.PREFIX}${it.replace("%reforge%", name)}" })
        }
    }.build()

    val stonePrice = config.getIntOrNull("stone.price") ?: -1

    init {
        Reforges.addNewReforge(this)

        ReforgeUtils.setReforgeStone(stone, this)

        Display.display(stone)

        if (config.getBool("stone.enabled")) {
            CustomItem(
                plugin.namespacedKeyFactory.create("stone_" + this.id),
                { test -> ReforgeUtils.getReforgeStone(test) == this },
                stone
            ).register()

            if (config.getBool("stone.craftable")) {
                Recipes.createAndRegisterRecipe(
                    plugin,
                    "stone_" + this.id,
                    stone,
                    config.getStrings("stone.recipe")
                )
            }
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