package com.willfp.reforges;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.eco.core.display.DisplayModule;
import com.willfp.eco.core.integrations.IntegrationLoader;
import com.willfp.eco.core.items.Items;
import com.willfp.reforges.commands.CommandReforge;
import com.willfp.reforges.commands.CommandReforges;
import com.willfp.reforges.config.ReforgesJson;
import com.willfp.reforges.config.TargetYml;
import com.willfp.reforges.display.ReforgesDisplay;
import com.willfp.reforges.effects.Effect;
import com.willfp.reforges.effects.Effects;
import com.willfp.reforges.integrations.aureliumskills.AureliumSkillsIntegration;
import com.willfp.reforges.integrations.ecoskills.EcoSkillsIntegration;
import com.willfp.reforges.integrations.talismans.TalismansIntegration;
import com.willfp.reforges.reforges.Reforges;
import com.willfp.reforges.reforges.util.ReforgeArgParser;
import com.willfp.reforges.reforges.util.ReforgeEnableListeners;
import com.willfp.reforges.reforges.util.ReforgeLookup;
import com.willfp.reforges.reforges.util.WatcherTriggers;
import com.willfp.reforges.util.AntiPlaceListener;
import com.willfp.reforges.util.DiscoverRecipeListener;
import com.willfp.reforges.vault.EconomyHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

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
        this.targetYml = new TargetYml(this);
        this.reforgesJson = new ReforgesJson(this);
        instance = this;
    }

    @Override
    protected void handleEnable() {
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
            this.getScheduler().run(() -> this.getEventManager().registerListener(effect));
        }
        this.getLogger().info(Reforges.values().size() + " Reforges Loaded");
        this.getScheduler().runTimer(() -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                ReforgeLookup.updateReforges(player);
            }
        }, 81, 81);
    }

    @Override
    protected List<Listener> loadListeners() {
        return Arrays.asList(
                new DiscoverRecipeListener(this),
                new AntiPlaceListener(),
                new WatcherTriggers(this),
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
        return Arrays.asList(
                new IntegrationLoader("EcoSkills", EcoSkillsIntegration.INSTANCE::load),
                new IntegrationLoader("Talismans", TalismansIntegration.INSTANCE::registerProvider),
                new IntegrationLoader("AureliumSkills", AureliumSkillsIntegration.INSTANCE::load)
        );
    }

    @Override
    public String getMinimumEcoVersion() {
        return "6.9.0";
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
