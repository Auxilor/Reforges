package com.willfp.reforges.effects;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.willfp.reforges.effects.effects.EffectAttackSpeedMultiplier;
import com.willfp.reforges.effects.effects.EffectCritMultiplier;
import com.willfp.reforges.effects.effects.EffectDamageMultiplier;
import com.willfp.reforges.effects.effects.EffectDurabilityMultiplier;
import com.willfp.reforges.effects.effects.EffectIncomingDamageMultiplier;
import com.willfp.reforges.effects.effects.EffectKnockbackMultiplier;
import com.willfp.reforges.effects.effects.EffectRewardBlockBreak;
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
    private static final BiMap<String, Effect> BY_ID = HashBiMap.create();

    public static final Effect DAMAGE_MULTIPLIER = new EffectDamageMultiplier();
    public static final Effect CRIT_MULTIPLIER = new EffectCritMultiplier();
    public static final Effect REWARD_KILL = new EffectRewardKill();
    public static final Effect KNOCKBACK_MULTIPLIER = new EffectKnockbackMultiplier();
    public static final Effect REWARD_BLOCK_BREAK = new EffectRewardBlockBreak();
    public static final Effect INCOMING_DAMAGE_MULTIPLIER = new EffectIncomingDamageMultiplier();
    public static final Effect ATTACK_SPEED_MULTIPLIER = new EffectAttackSpeedMultiplier();
    public static final Effect DURABILITY_MULTIPLIER = new EffectDurabilityMultiplier();

    /**
     * Get effect matching id.
     *
     * @param id The id to query.
     * @return The matching effect, or null if not found.
     */
    @Nullable
    public static Effect getByID(@NotNull final String id) {
        return BY_ID.get(id);
    }

    /**
     * List of all registered effects.
     *
     * @return The effects.
     */
    public static List<Effect> values() {
        return ImmutableList.copyOf(BY_ID.values());
    }

    /**
     * Add new effect to EcoWeapons.
     *
     * @param effect The effect to add.
     */
    public static void addNewEffect(@NotNull final Effect effect) {
        BY_ID.remove(effect.getId());
        BY_ID.put(effect.getId(), effect);
    }
}