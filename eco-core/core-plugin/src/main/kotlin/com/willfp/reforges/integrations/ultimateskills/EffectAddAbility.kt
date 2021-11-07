package com.willfp.reforges.integrations.ultimateskills

import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.eco.util.NumberUtils
import com.willfp.reforges.ReforgesPlugin
import com.willfp.reforges.effects.Effect
import mc.ultimatecore.skills.HyperSkills
import mc.ultimatecore.skills.objects.abilities.Ability
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

class EffectAddAbility : Effect("add_ultimateskills_ability") {
    override fun handleEnable(
        player: Player,
        config: JSONConfig
    ) {
        HyperSkills.getInstance().api.addAbility(
            player.uniqueId,
            Ability.valueOf(config.getString("ability", false)),
            config.getDouble("amount")
        )
        player.persistentDataContainer.set(
            ReforgesPlugin.getInstance().namespacedKeyFactory.create("addAbility"),
            PersistentDataType.STRING,
            NumberUtils.format(config.getDouble("amount")) + "::" + config.getString("ability", false)
        )
    }

    override fun handleDisable(player: Player) {
        player.persistentDataContainer.get(ReforgesPlugin.getInstance().namespacedKeyFactory.create("addAbility"), PersistentDataType.STRING)
            ?.let {
                HyperSkills.getInstance().api.removeAbility(
                    player.uniqueId,
                    Ability.valueOf(it.split("::")[1]),
                    it.split("::")[0].toDouble()
                )
            }
    }
}