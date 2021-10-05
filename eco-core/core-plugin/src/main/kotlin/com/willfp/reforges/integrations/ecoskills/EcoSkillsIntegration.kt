package com.willfp.reforges.integrations.ecoskills

import com.willfp.reforges.effects.Effect

object EcoSkillsIntegration {
    lateinit var ADD_STAT: Effect

    fun load() {
        ADD_STAT = EffectAddStat()
    }
}