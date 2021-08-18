package com.willfp.reforges;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.eco.core.display.DisplayModule;
import com.willfp.reforges.commands.CommandReforge;
import com.willfp.reforges.commands.CommandReforges;
import com.willfp.reforges.display.ReforgesDisplay;
import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.Reforges;
import com.willfp.reforges.util.AntiPlaceListener;
import com.willfp.reforges.util.DiscoverRecipeListener;
import com.willfp.reforges.vault.EconomyHandler;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class ReforgesPlugin extends EcoPlugin {
    /**
     * Instance of Reforges.
     */
    @Getter
    private static ReforgesPlugin instance;

    /**
     * Internal constructor called by bukkit on plugin load.
     */
    public ReforgesPlugin() {
        super(0, 12412, "&3", true);
        instance = this;
    }

    @Override
    protected void handleEnable() {
        this.getLogger().info(Reforges.values().size() + " Reforges Loaded");
    }

    @Override
    protected void handleAfterLoad() {
        EconomyHandler.setEnabled(EconomyHandler.init());

        if (!EconomyHandler.isEnabled()) {
            this.getLogger().severe("Vault economy support not enabled");
        }
    }

    @Override
    protected void handleReload() {
        for (Reforge reforge : Reforges.values()) {
            HandlerList.unregisterAll(reforge);
            this.getScheduler().runLater(() -> {
                if (reforge.isEnabled()) {
                    this.getEventManager().registerListener(reforge);
                }
            }, 1);
        }
    }

    @Override
    protected List<Listener> loadListeners() {
        return Arrays.asList(
                new DiscoverRecipeListener(this),
                new AntiPlaceListener(this)
        );
    }

    @Override
    protected List<PluginCommand> loadPluginCommands() {
        return Arrays.asList(
                new CommandReforge(this),
                new CommandReforges(this)
        );
    }

    @Override
    protected @Nullable DisplayModule createDisplayModule() {
        return new ReforgesDisplay(this);
    }

    @Override
    public String getMinimumEcoVersion() {
        return "6.4.0";
    }
}
