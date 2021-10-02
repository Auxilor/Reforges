package com.willfp.reforges.conditions;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.willfp.reforges.conditions.conditions.ConditionAboveHealthPercent;
import com.willfp.reforges.conditions.conditions.ConditionAboveHungerPercent;
import com.willfp.reforges.conditions.conditions.ConditionAboveXPLevel;
import com.willfp.reforges.conditions.conditions.ConditionAboveY;
import com.willfp.reforges.conditions.conditions.ConditionBelowHealthPercent;
import com.willfp.reforges.conditions.conditions.ConditionBelowHungerPercent;
import com.willfp.reforges.conditions.conditions.ConditionBelowXPLevel;
import com.willfp.reforges.conditions.conditions.ConditionBelowY;
import com.willfp.reforges.conditions.conditions.ConditionHasPermission;
import com.willfp.reforges.conditions.conditions.ConditionInAir;
import com.willfp.reforges.conditions.conditions.ConditionInBiome;
import com.willfp.reforges.conditions.conditions.ConditionInWater;
import com.willfp.reforges.conditions.conditions.ConditionInWorld;
import com.willfp.reforges.conditions.conditions.ConditionIsSneaking;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@UtilityClass
@SuppressWarnings("unused")
public class Conditions {
    /**
     * All registered conditions.
     */
    private static final BiMap<String, Condition> BY_ID = HashBiMap.create();

    public static final Condition BELOW_Y = new ConditionBelowY();
    public static final Condition ABOVE_Y = new ConditionAboveY();
    public static final Condition ABOVE_HEALTH_PERCENT = new ConditionAboveHealthPercent();
    public static final Condition BELOW_HEALTH_PERCENT = new ConditionBelowHealthPercent();
    public static final Condition IN_WATER = new ConditionInWater();
    public static final Condition IN_WORLD = new ConditionInWorld();
    public static final Condition ABOVE_XP_LEVEL = new ConditionAboveXPLevel();
    public static final Condition BELOW_XP_LEVEL = new ConditionBelowXPLevel();
    public static final Condition ABOVE_HUNGER_PERCENT = new ConditionAboveHungerPercent();
    public static final Condition BELOW_HUNGER_PERCENT = new ConditionBelowHungerPercent();
    public static final Condition IN_BIOME = new ConditionInBiome();
    public static final Condition HAS_PERMISSION = new ConditionHasPermission();
    public static final Condition IS_SNEAKING = new ConditionIsSneaking();
    public static final Condition IN_AIR = new ConditionInAir();

    /**
     * Get condition matching name.s
     *
     * @param name The name to query.
     * @return The matching condition, or null if not found.
     */
    public static Condition getByID(@NotNull final String name) {
        return BY_ID.get(name);
    }

    /**
     * List of all registered conditions.
     *
     * @return The conditions.
     */
    public static List<Condition> values() {
        return ImmutableList.copyOf(BY_ID.values());
    }

    /**
     * Add new condition to EcoArmor.
     *
     * @param condition The condition to add.
     */
    public static void addNewCondition(@NotNull final Condition condition) {
        BY_ID.remove(condition.getId());
        BY_ID.put(condition.getId(), condition);
    }
}
