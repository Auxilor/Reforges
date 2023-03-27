package com.willfp.reforges.util

import com.github.benmanes.caffeine.cache.Caffeine
import com.willfp.libreforge.ItemProvidedHolder
import com.willfp.reforges.ReforgesPlugin
import com.willfp.reforges.reforges.ReforgeTarget
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.concurrent.TimeUnit

typealias SlotProvider = (Player) -> Map<ItemStack?, ReforgeTarget.Slot?>

object ReforgeLookup {
    private val plugin = ReforgesPlugin.instance
    private val slotProviders = mutableSetOf<(Player) -> Map<ItemStack, ReforgeTarget.Slot>>()

    private val itemCache = Caffeine.newBuilder()
        .expireAfterWrite(2, TimeUnit.SECONDS)
        .build<Player, Map<ItemStack, ReforgeTarget.Slot>>()

    private val reforgeCache = Caffeine.newBuilder()
        .expireAfterWrite(2, TimeUnit.SECONDS)
        .build<Player, Collection<ItemProvidedHolder>>()

    @JvmStatic
    fun registerProvider(provider: SlotProvider) {
        slotProviders.add {
            val found = mutableMapOf<ItemStack, ReforgeTarget.Slot>()
            for ((item, slot) in provider(it)) {
                if (item != null && slot != null) {
                    found[item] = slot
                }
            }
            found
        }
    }

    private fun provide(player: Player): Map<ItemStack, ReforgeTarget.Slot> {
        return itemCache.get(player) {
            val found = mutableMapOf<ItemStack, ReforgeTarget.Slot>()
            for (provider in slotProviders) {
                found.putAll(provider(player))
            }

            found
        }
    }

    fun provideReforges(player: Player): List<ItemProvidedHolder> {
        return reforgeCache.get(player) {
            val found = mutableListOf<ItemProvidedHolder>()

            for ((itemStack, slot) in provide(player)) {
                val reforge = itemStack.reforge ?: continue
                if (slot != ReforgeTarget.Slot.ANY) {
                    if (!reforge.targets.map { it.slot }.contains(slot)) {
                        continue
                    }
                }
                found.add(
                    ItemProvidedHolder(
                        reforge,
                        itemStack
                    )
                )
            }

            found
        }.toList()
    }

    fun clearCache(player: Player) {
        itemCache.invalidate(player)
        reforgeCache.invalidate(player)
    }

    init {
        registerProvider {
            mapOf(
                Pair(
                    it.inventory.itemInMainHand,
                    ReforgeTarget.Slot.HANDS
                )
            )
        }

        if (!plugin.configYml.getBool("no-offhand")) {
            registerProvider {
                mapOf(
                    Pair(
                        it.inventory.itemInOffHand,
                        ReforgeTarget.Slot.HANDS
                    )
                )
            }
        }


        registerProvider {
            val items = mutableMapOf<ItemStack?, ReforgeTarget.Slot?>()
            for (stack in it.inventory.armorContents) {
                items[stack] = ReforgeTarget.Slot.ARMOR
            }
            items
        }
    }
}
