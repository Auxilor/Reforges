package com.willfp.reforges.reforges

import com.willfp.eco.core.config.config
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.display.Display
import com.willfp.eco.core.items.CustomItem
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.builder.ItemStackBuilder
import com.willfp.eco.core.price.ConfiguredPrice
import com.willfp.eco.core.recipe.Recipes
import com.willfp.eco.core.registry.Registrable
import com.willfp.eco.util.StringUtils
import com.willfp.libreforge.Holder
import com.willfp.libreforge.ItemProvidedHolder
import com.willfp.libreforge.ViolationContext
import com.willfp.libreforge.conditions.Conditions
import com.willfp.libreforge.effects.Effects
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.reforges.ReforgesPlugin
import com.willfp.reforges.util.reforgeStone
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.Objects

@Suppress("DEPRECATION")
class Reforge(
    id: String,
    internal val config: Config,
    plugin: ReforgesPlugin
) : Holder, Registrable {
    val name = config.getFormattedString("name")

    val namePrefixComponent = StringUtils.toComponent("$name ").decoration(TextDecoration.ITALIC, false)

    val description: List<String> = config.getStrings("description")

    val targets = config.getStrings("targets").mapNotNull { ReforgeTargets.getByName(it) }.toSet()

    override val effects = Effects.compile(
        config.getSubsections("effects"),
        ViolationContext(plugin, "Reforge $id")
    )

    override val conditions = Conditions.compile(
        config.getSubsections("conditions"),
        ViolationContext(plugin, "Reforge $id")
    )

    override val id = plugin.createNamespacedKey(id)

    val requiresStone = config.getBool("stone.enabled")

    val stone: ItemStack = ItemStackBuilder(Items.lookup(config.getString("stone.item")).item).apply {
        if (config.getBool("stone.enabled")) {
            setDisplayName(config.getFormattedString("stone.name").replace("%reforge%", name))
            addLoreLines(
                config.getFormattedStrings("stone.lore").map { "${Display.PREFIX}${it.replace("%reforge%", name)}" })
        }
    }.build()

    val stonePrice = if (config.has("stone.price")) {
        when {
            // Legacy support
            config.getDouble("stone.price") > 0 -> {
                ConfiguredPrice.createOrFree(
                    config {
                        "value" to config.getDouble("stone.price")
                        "type" to "coins"
                        "display" to "%value%"
                    }
                )
            }

            else -> ConfiguredPrice.createOrFree(config.getSubsection("stone.price"))
        }
    } else null

    private val onReforgeEffects = Effects.compileChain(
        config.getSubsections("on-reforge-effects"),
        ViolationContext(plugin, "Reforge $id").with("on-reforge-effects")
    )

    init {
        stone.reforgeStone = this

        if (config.getBool("stone.enabled")) {
            CustomItem(
                plugin.namespacedKeyFactory.create("stone_" + this.id),
                { test -> test.reforgeStone == this },
                stone
            ).register()

            if (config.getBool("stone.craftable")) {
                Recipes.createAndRegisterRecipe(
                    plugin,
                    "stone_" + this.id.key,
                    stone,
                    config.getStrings("stone.recipe")
                )
            }
        }
    }

    fun canBeAppliedTo(item: ItemStack?): Boolean {
        return targets.any { target -> target.items.any { it.matches(item) } }
    }

    fun runOnReforgeEffects(player: Player, item: ItemStack) {
        onReforgeEffects?.trigger(
            player.toDispatcher(),
            TriggerData(
                holder = ItemProvidedHolder(this, item),
                player = player,
                item = item
            )
        )
    }

    override fun getID(): String {
        return this.id.key
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