package com.willfp.reforges.integrations.ultimateskills

import com.willfp.eco.core.integrations.Integration
import com.willfp.reforges.effects.Effect

object UltimateSkillsIntegration : Integration {
    private lateinit var ADD_PERK: Effect
    private lateinit var ADD_ABILITY: Effect

    @JvmStatic
    fun load() {
        ADD_PERK = EffectAddPerk()
        ADD_ABILITY = EffectAddAbility()
    }

    override fun getPluginName(): String {
        return "UltimateSkills"
    }
}