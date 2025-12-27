package com.jeffyjamzhd.btj.api.hook;

import net.minecraft.src.EntityPlayer;

public interface ExhaustionEvent {
    /**
     * Triggered upon exhaustion being added to {@link net.minecraft.src.FoodStats}
     * @param player Player who is exhausted
     * @param exhaustion Amount of exhaustion
     */
    void onExhaustion(EntityPlayer player, float exhaustion);
}
