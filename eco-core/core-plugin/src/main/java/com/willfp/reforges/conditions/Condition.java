package com.willfp.reforges.conditions;

import com.willfp.eco.core.config.interfaces.JSONConfig;
import com.willfp.reforges.ReforgesPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class Condition implements Listener {
    /**
     * Instance of Reforges.
     */
    @Getter(AccessLevel.PROTECTED)
    private final ReforgesPlugin plugin = ReforgesPlugin.getInstance();

    /**
     * The name of the condition.
     */
    @Getter
    private final String id;

    /**
     * Create a new condition.
     *
     * @param id The condition id.
     */
    protected Condition(@NotNull final String id) {
        this.id = id;

        Conditions.addNewCondition(this);
    }

    /**
     * Get if condition is met for a player.
     *
     * @param player The player.
     * @param config The config of the condition.
     * @return If met.
     */
    public abstract boolean isConditionMet(@NotNull Player player,
                                           @NotNull JSONConfig config);

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Condition condition)) {
            return false;
        }
        return getId().equals(condition.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
