package com.willfp.reforges;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.eco.core.display.DisplayModule;
import com.willfp.reforges.commands.CommandReforge;
import com.willfp.reforges.display.ReforgesDisplay;
import lombok.Getter;
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
        super(0, 12412, "&#00ff00");
        instance = this;
    }

    @Override
    protected List<Listener> loadListeners() {
        return Arrays.asList(

        );
    }

    @Override
    protected List<PluginCommand> loadPluginCommands() {
        return Arrays.asList(
                new CommandReforge(this)
        );
    }

    @Override
    protected @Nullable DisplayModule createDisplayModule() {
        return new ReforgesDisplay(this);
    }
}
