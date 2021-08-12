package com.willfp.reforges.reforges;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.Prerequisite;
import com.willfp.reforges.ReforgesPlugin;
import com.willfp.reforges.config.ReforgeConfig;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class Reforge {
    /**
     * Instance of Reforges for reforges to be able to access.
     */
    @Getter(AccessLevel.PROTECTED)
    private final EcoPlugin plugin = ReforgesPlugin.getInstance();

    /**
     * The key to store reforges in meta.
     */
    @Getter
    private final String key;

    /**
     * The reforges config.
     */
    @Getter
    private final ReforgeConfig config;

    /**
     * If the reforge is enabled.
     */
    @Getter
    private boolean enabled;

    /**
     * Create a new Reforge.
     *
     * @param key           The key name of the reforge..
     * @param prerequisites Optional {@link Prerequisite}s that must be met.
     */
    protected Reforge(@NotNull final String key,
                      @NotNull final Prerequisite... prerequisites) {
        this.key = key;
        this.config = new ReforgeConfig(this.getKey(), this.getClass(), this.plugin);

        if (!Prerequisite.areMet(prerequisites)) {
            return;
        }

        Reforges.addNewReforge(this);
        this.update();
    }

    /**
     * Update the reforge based off config values.
     * This can be overridden but may lead to unexpected behavior.
     */
    public void update() {
        enabled = config.getBool("enabled");

        postUpdate();
    }

    protected void postUpdate() {
        // Unused as some talismans may have postUpdate tasks, however most won't.
    }

    /**
     * Get the reforge target.
     */
    public abstract ReforgeTarget getTarget();

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Reforge reforge)) {
            return false;
        }

        return this.getKey().equals(reforge.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getKey());
    }

    @Override
    public String toString() {
        return "Reforge{"
                + this.getKey()
                + "}";
    }
}
