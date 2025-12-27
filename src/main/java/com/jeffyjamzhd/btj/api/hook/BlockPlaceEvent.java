package com.jeffyjamzhd.btj.api.hook;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;

public interface BlockPlaceEvent {
    /**
     * Triggered upon successful block placement
     * @param player Player who placed block
     * @param block Block that was placed
     */
    void onBlockPlace(EntityPlayer player, Block block);
}
