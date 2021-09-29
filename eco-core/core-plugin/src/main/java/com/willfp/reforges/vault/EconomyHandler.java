package com.willfp.reforges.vault;

import lombok.Setter;
import lombok.experimental.UtilityClass;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

@UtilityClass
public final class EconomyHandler {
    /**
     * The instance.
     */
    private static Economy instance = null;

    @Setter
    private static boolean enabled = false;

    /**
     * Initialize the economy manager.
     *
     * @return If was successful.
     */
    public static boolean init() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        instance = rsp.getProvider();
        return true;
    }

    public static Economy getInstance() {
        return EconomyHandler.instance;
    }

    public static boolean isEnabled() {
        return EconomyHandler.enabled;
    }
}
