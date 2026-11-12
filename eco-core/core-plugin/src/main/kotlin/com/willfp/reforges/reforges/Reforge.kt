package com.willfp.reforges.reforges

import com.willfp.reforges.plugin
import com.willfp.reforges.util.reforgeStone
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import wiki.ecohub.config.Config
import wiki.ecohub.config.ViolationContext
import wiki.ecohub.config.config
import wiki.ecohub.core.price.ConfiguredPrice
import wiki.ecohub.core.registry.Registrable
import wiki.ecohub.game.entity.toDispatcher
import wiki.ecohub.game.item.CustomItem
import wiki.ecohub.game.item.Items
import wiki.ecohub.game.item.builder.ItemStackBuilder
import wiki.ecohub.modules.recipe.Recipes
import wiki.ecohub.modules.recipe.crafting.CraftingRecipe
import wiki.ecohub.rules.condition.Conditions
import wiki.ecohub.rules.effect.Effects
import wiki.ecohub.rules.holder.Holder
import wiki.ecohub.rules.trigger.TriggerData
import wiki.ecohub.ui.display.Display
import wiki.ecohub.ui.text.StringUtils
import java.util.Objects

@Suppress("DEPRECATION")
class Reforge(
    id: String,
    internal val config: Config
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

    private val stoneTemplate: ItemStack = ItemStackBuilder(
        Items.lookup(config.getString("stone.item")).getItem()
    ).apply {
        if (config.getBool("stone.enabled")) {
            setDisplayName(config.getFormattedString("stone.name").replace("%reforge%", name))
            addLoreLines(
                config.getFormattedStrings("stone.lore").map { "${Display.PREFIX}${it.replace("%reforge%", name)}" })
        }
    }.build()

    val stone: ItemStack
        get() = stoneTemplate.clone()

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

    val stoneCostExponent = config.getDoubleOrNull("stone.cost-exponent") ?: 1.0

    private val onReforgeEffects = Effects.compileChain(
        config.getSubsections("on-reforge-effects"),
        ViolationContext(plugin, "Reforge $id").with("on-reforge-effects")
    )

    init {
        stoneTemplate.reforgeStone = this

        if (config.getBool("stone.enabled")) {
            CustomItem(
                plugin.namespacedKeyFactory.create("stone_" + this.id.key),
                { test -> test.reforgeStone == this },
                stoneTemplate
            ).register()

            val stoneRecipe: CraftingRecipe? = config.getBool("stone.craftable")
                .takeIf { it }
                ?.let {
                    Recipes.createAndRegisterRecipe(
                        plugin,
                        "stone_" + this.id.key,
                        stoneTemplate,
                        config.getStrings("stone.recipe"),
                        config.getStringOrNull("stone.recipe-permission"),
                        config.getBool("stone.shapeless")
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
