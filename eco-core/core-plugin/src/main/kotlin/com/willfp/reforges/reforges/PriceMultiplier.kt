package com.willfp.reforges.reforges

import org.bukkit.Bukkit
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault

data class PriceMultiplier(
    val permission: String,
    val multiplier: Double,
    val priority: Int
) {
    init {
        if (Bukkit.getPluginManager().getPermission(permission) == null) {
            Bukkit.getPluginManager().addPermission(
                Permission(
                    permission,
                    "Gives a ${multiplier}x price multiplier when reforging",
                    PermissionDefault.FALSE
                )
            )
        }
    }
}
