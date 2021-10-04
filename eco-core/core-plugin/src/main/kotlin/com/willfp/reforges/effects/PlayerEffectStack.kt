@file:JvmName("PlayerEffectStack")

package com.willfp.reforges.effects

import org.bukkit.entity.Player
import java.util.*

private val effectStack = mutableMapOf<UUID, MutableMap<Effect, Int>>()

fun Player.pushEffect(effect: Effect) {
    val stack = effectStack[this.uniqueId] ?: mutableMapOf()
    var amount = stack[effect] ?: 0
    amount++
    stack[effect] = amount
    effectStack[this.uniqueId] = stack
}

fun Player.popEffect(effect: Effect) {
    val stack = effectStack[this.uniqueId] ?: mutableMapOf()
    var amount = stack[effect] ?: 0
    amount--
    stack[effect] = if (amount < 0) 0 else amount
    effectStack[this.uniqueId] = stack
}

fun Player.getEffectAmount(effect: Effect): Int {
    val stack = effectStack[this.uniqueId] ?: mutableMapOf()
    return stack[effect] ?: 0
}
