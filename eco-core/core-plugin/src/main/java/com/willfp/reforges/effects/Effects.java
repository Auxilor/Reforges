package com.willfp.reforges.effects;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.willfp.reforges.effects.effects.EffectCritMultiplier;
import com.willfp.reforges.effects.effects.EffectDamageMultiplier;
import com.willfp.reforges.effects.effects.EffectKnockbackMultiplier;
import com.willfp.reforges.effects.effects.EffectRewardKill;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@UtilityClass
@SuppressWarnings("unused")
public class Effects {
    /**
     * All registered effects.
     */
    private static final BiMap<String, Effect> BY_NAME = HashBiMap.create();

    public static final Effect DAMAGE_MULTIPLIER = new EffectDamageMultiplier();
    public static final Effect CRIT_MULTIPLIER = new EffectCritMultiplier();
    public static final Effect REWARD_KILL = new EffectRewardKill();
    public static final Effect KNOCKBACK_MULTIPLIER = new EffectKnockbackMultiplier();

    /**
     * Get effect matching name.
     *
     * @param name The name to query.
     * @return The matching effect, or null if not found.
     */
    @Nullable
    public static Effect getByName(@NotNull final String name) {
        return BY_NAME.get(name);
    }

    /**
     * List of all registered effects.
     *
     * @return The effects.
     */
    public static List<Effect> values() {
        return ImmutableList.copyOf(BY_NAME.values());
    }

    /**
     * Add new effect to EcoWeapons.
     *
     * @param effect The effect to add.
     */
    public static void addNewEffect(@NotNull final Effect effect) {
        BY_NAME.remove(effect.getName());
        BY_NAME.put(effect.getName(), effect);
    }
}