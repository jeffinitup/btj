package com.jeffyjamzhd.btj.api.hook;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;

public interface FoodConsumedEvent {
    /**
     * Triggered upon the consumption of food
     * @param player Player who consumed food
     * @param item ItemStack of food
     */
    void onFoodConsume(EntityPlayer player, ItemStack item);
}
