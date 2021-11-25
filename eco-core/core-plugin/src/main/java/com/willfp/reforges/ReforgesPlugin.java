package com.willfp.reforges;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.eco.core.display.DisplayModule;
import com.willfp.eco.core.integrations.IntegrationLoader;
import com.willfp.eco.core.items.Items;
import com.willfp.libreforge.LibReforge;
import com.willfp.libreforge.effects.ConfiguredEffect;
import com.willfp.reforges.commands.CommandReforge;
import com.willfp.reforges.commands.CommandReforges;
import com.willfp.reforges.config.ReforgesJson;
import com.willfp.reforges.config.TargetYml;
import com.willfp.reforges.display.ReforgesDisplay;
import com.willfp.reforges.integrations.talismans.TalismansIntegration;
import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.Reforges;
import com.willfp.reforges.reforges.util.ReforgeArgParser;
import com.willfp.reforges.reforges.util.ReforgeEnableListeners;
import com.willfp.reforges.util.AntiPlaceListener;
import com.willfp.reforges.util.DiscoverRecipeListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReforgesPlugin extends EcoPlugin {
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
     * reforges.json.
     */
    @Getter
    private final ReforgesJson reforgesJson;

    /**
     * Internal constructor called by bukkit on plugin load.
     */
    public ReforgesPlugin() {
        super(1330, 12412, "&3", true);
        LibReforge.init(this);
        this.targetYml = new TargetYml(this);
        this.reforgesJson = new ReforgesJson(this);
        instance = this;
    }

    @Override
    protected void handleEnable() {
        LibReforge.enable(this);
        Items.registerArgParser(new ReforgeArgParser());
    }

    @Override
    protected void handleDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Reforge value : Reforges.values()) {
                for (ConfiguredEffect effect : value.getEffects()) {
                    effect.getEffect().disableForPlayer(player);
                }
            }
        }
    }

    @Override
    protected void handleReload() {
        this.getLogger().info(Reforges.values().size() + " Reforges Loaded");
        LibReforge.reload(this);
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

    @Override
    protected List<IntegrationLoader> loadIntegrationLoaders() {
        List<IntegrationLoader> loaders = new ArrayList<>();

        loaders.addAll(LibReforge.getIntegrationLoaders());

        loaders.addAll(
                Arrays.asList(
                        new IntegrationLoader("Talismans", TalismansIntegration::registerProvider)
                )
        );

        return loaders;
    }

    @Override
    public String getMinimumEcoVersion() {
        return "6.12.2";
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
