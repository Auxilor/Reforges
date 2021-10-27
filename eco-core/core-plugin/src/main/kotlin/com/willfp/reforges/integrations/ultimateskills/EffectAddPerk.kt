package com.willfp.reforges.integrations.ultimateskills

import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.eco.util.NumberUtils
import com.willfp.reforges.ReforgesPlugin
import com.willfp.reforges.effects.Effect
import mc.ultimatecore.skills.HyperSkills
import mc.ultimatecore.skills.objects.perks.Perk
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

class EffectAddPerk : Effect("add_ultimateskills_perk") {
    override fun handleEnable(
        player: Player,
        config: JSONConfig
    ) {
        HyperSkills.getInstance().api.addPerk(
            player.uniqueId,
            Perk.valueOf(config.getString("perk", false)),
            config.getDouble("amount")
        )
        player.persistentDataContainer.set(ReforgesPlugin.getInstance().namespacedKeyFactory.create("addPerk"), PersistentDataType.STRING,
            NumberUtils.format(config.getDouble("amount")) + "::" + config.getString("perk", false)
        )
    }

    override fun handleDisable(player: Player) {
        player.persistentDataContainer.get(ReforgesPlugin.getInstance().namespacedKeyFactory.create("addPerk"), PersistentDataType.STRING)
            ?.let {
                HyperSkills.getInstance().api.removePerk(
                    player.uniqueId,
                    Perk.valueOf(it.split("::")[1]),
                    it.split("::")[0].toDouble()
                )
            }
    }
}