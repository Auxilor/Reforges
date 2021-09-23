package com.willfp.reforges;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.eco.core.display.DisplayModule;
import com.willfp.eco.core.items.Items;
import com.willfp.reforges.commands.CommandReforge;
import com.willfp.reforges.commands.CommandReforges;
import com.willfp.reforges.config.ReforgesJson;
import com.willfp.reforges.config.TargetYml;
import com.willfp.reforges.display.ReforgesDisplay;
import com.willfp.reforges.effects.Effect;
import com.willfp.reforges.effects.Effects;
import com.willfp.reforges.reforges.Reforges;
import com.willfp.reforges.reforges.util.ReforgeArgParser;
import com.willfp.reforges.reforges.util.WatcherTriggers;
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
     * target.yml.
     */
    @Getter
    private final TargetYml targetYml;

    /**
     * reforges.json.
     */
    @Getter
    private final ReforgesJson reforgesJson;

    /**
     * Internal constructor called by bukkit on plugin load.
     */
    public ReforgesPlugin() {
        super(1330, 12412, "&3", true);
        this.targetYml = new TargetYml(this);
        this.reforgesJson = new ReforgesJson(this);
        instance = this;
    }

    @Override
    protected void handleEnable() {
        this.getLogger().info(Reforges.values().size() + " Reforges Loaded");
        Items.registerArgParser(new ReforgeArgParser());
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
        for (Effect effect : Effects.values()) {
            HandlerList.unregisterAll(effect);
            this.getScheduler().run(() -> {
                    this.getEventManager().registerListener(effect);
            });
        }
    }

    @Override
    protected List<Listener> loadListeners() {
        return Arrays.asList(
                new DiscoverRecipeListener(this),
                new AntiPlaceListener(this),
                new WatcherTriggers(this)
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
        return "6.7.0";
    }
}
