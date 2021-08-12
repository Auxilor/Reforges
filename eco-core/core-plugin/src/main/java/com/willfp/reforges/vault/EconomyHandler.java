package com.willfp.reforges.vault;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

@UtilityClass
public class EconomyHandler {
    /**
     * The instance.
     */
    @Getter
    private static Economy instance = null;

    /**
     * Initialize the economy manager.
     *
     * @return If was successful.
     */
    public boolean init() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        instance = rsp.getProvider();
        return true;
    }
}
