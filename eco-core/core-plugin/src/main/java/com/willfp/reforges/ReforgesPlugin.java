package com.willfp.reforges;

import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.eco.core.display.DisplayModule;
import com.willfp.eco.core.integrations.IntegrationLoader;
import com.willfp.eco.core.items.Items;
import com.willfp.libreforge.LibReforgePlugin;
import com.willfp.reforges.commands.CommandReforge;
import com.willfp.reforges.commands.CommandReforges;
import com.willfp.reforges.config.ReforgesYml;
import com.willfp.reforges.config.TargetYml;
import com.willfp.reforges.display.ReforgesDisplay;
import com.willfp.reforges.integrations.talismans.TalismansIntegration;
import com.willfp.reforges.reforges.Reforges;
import com.willfp.reforges.reforges.util.ReforgeArgParser;
import com.willfp.reforges.reforges.util.ReforgeEnableListeners;
import com.willfp.reforges.reforges.util.ReforgeLookup;
import com.willfp.reforges.util.AntiPlaceListener;
import com.willfp.reforges.util.DiscoverRecipeListener;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class ReforgesPlugin extends LibReforgePlugin {
    /**
     * Instance of Reforges.
     */
    private static ReforgesPlugin instance;

    /**
     * target.yml.
     */
    @Getter
    private final TargetYml targetYml;

    /**
     * reforges.yml.
     */
    @Getter
    private final ReforgesYml reforgesYml;

    /**
     * Internal constructor called by bukkit on plugin load.
     */
    public ReforgesPlugin() {
        super(1330, 12412, "&3", "");
        this.targetYml = new TargetYml(this);
        this.reforgesYml = new ReforgesYml(this);
        instance = this;

        registerHolderProvider(ReforgeLookup::provideReforges);
    }

    @Override
    public void handleEnableAdditional() {
        Items.registerArgParser(new ReforgeArgParser());
    }

    @Override
    public void handleReloadAdditional() {
        this.getLogger().info(Reforges.values().size() + " Reforges Loaded");
    }


    @Override
    protected List<Listener> loadListeners() {
        return Arrays.asList(
                new DiscoverRecipeListener(this),
                new AntiPlaceListener(),
                new ReforgeEnableListeners(this)
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

    @NotNull
    @Override
    public List<IntegrationLoader> loadAdditionalIntegrations() {
        return Arrays.asList(
                new IntegrationLoader("Talismans", TalismansIntegration::registerProvider)
        );
    }

    @Override
    @NotNull
    public String getMinimumEcoVersion() {
        return "6.35.1";
    }

    /**
     * Get an instance of Reforges.
     *
     * @return The instance.
     */
    public static ReforgesPlugin getInstance() {
        return instance;
    }
}
