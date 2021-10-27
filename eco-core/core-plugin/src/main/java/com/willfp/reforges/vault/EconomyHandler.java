package com.willfp.reforges.vault;

import com.willfp.reforges.ReforgesPlugin;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.concurrent.ExecutionException;

@UtilityClass
public final class EconomyHandler {
    /**
     * The instance.
     */
    private static Economy instance = null;

    @Setter
    private static boolean usePlayerPoints = false;

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

    /**
     * Get if a player has a specified amount.
     *
     * @param player The player.
     * @param amount The amount.
     * @return If a player has the amount.
     */
    public static boolean has(Player player, double amount) {
        if (usePlayerPoints && ReforgesPlugin.getInstance().getConfigYml().getBool("reforge.use-player-points")) {
            try {
                return PlayerPoints.getInstance().getAPI().lookAsync(player.getUniqueId()).get() >= amount;
            } catch (ExecutionException | InterruptedException e) {
                return false;
            }
        }
        return getInstance().has(player, amount);
    }

    public static boolean isEnabled() {
        return EconomyHandler.enabled;
    }
}
