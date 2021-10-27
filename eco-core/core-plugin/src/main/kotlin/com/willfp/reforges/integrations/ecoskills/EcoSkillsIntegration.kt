package com.willfp.reforges.integrations.ecoskills

import com.willfp.eco.core.integrations.Integration
import com.willfp.reforges.effects.Effect

object EcoSkillsIntegration : Integration {
    private lateinit var ADD_STAT: Effect

    @JvmStatic
    fun load() {
        ADD_STAT = EffectAddStat()
    }

    override fun getPluginName(): String {
        return "EcoSkills"
    }
}