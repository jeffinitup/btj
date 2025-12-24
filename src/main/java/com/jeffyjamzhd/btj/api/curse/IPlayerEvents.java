package com.jeffyjamzhd.btj.api.curse;

import api.world.BlockPos;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;

/**
 * Curses can extend from this for player centric callbacks
 */
public interface IPlayerEvents {
    /**
     * Triggered upon the consumption of food
     * @param player Player who consumed food
     * @param item ItemStack of food
     */
    default void onFoodConsume(EntityPlayer player, ItemStack item) {}

    /**
     * Triggered upon successful block placement
     * @param player Player who placed block
     * @param block Block that was placed
     */
    default void onBlockPlace(EntityPlayer player, Block block) {}

    /**
     * Triggered upon successful block break
     * @param player Player who broke block
     * @param block_id Block id that was broken
     * @param pos Block position
     * @param meta Block metadata
     */
    default void onBlockBreak(EntityPlayer player, int block_id, BlockPos pos, int meta) {}
}
