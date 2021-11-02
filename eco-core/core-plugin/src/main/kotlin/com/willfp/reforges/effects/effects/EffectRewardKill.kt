package com.willfp.reforges.effects.effects

import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.eco.core.events.EntityDeathByEntityEvent
import com.willfp.eco.core.integrations.economy.EconomyManager
import com.willfp.reforges.effects.Effect
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class EffectRewardKill : Effect("reward_kill") {
    override fun onKill(killer: Player,
                        victim: LivingEntity,
                        event: EntityDeathByEntityEvent,
                        config: JSONConfig) {
        EconomyManager.giveMoney(killer, config.getDouble("amount"))
    }
}