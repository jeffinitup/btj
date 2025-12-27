package com.jeffyjamzhd.btj.api.hook;

import api.world.BlockPos;
import net.minecraft.src.EntityPlayer;

public interface BlockBreakEvent {
    /**
     * Triggered upon successful block break
     * @param player Player who broke block
     * @param block_id Block id that was broken
     * @param pos Block position
     * @param meta Block metadata
     */
    void onBlockBreak(EntityPlayer player, int block_id, BlockPos pos, int meta);
}
