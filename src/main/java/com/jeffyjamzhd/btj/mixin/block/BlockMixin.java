package com.jeffyjamzhd.btj.mixin.block;

import btw.world.util.BlockPos;
import com.jeffyjamzhd.btj.api.CurseManager;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Block.class)
public class BlockMixin {
    @Inject(method = "harvestBlock", at = @At("TAIL"))
    private void harvestBlockCallback(World world, EntityPlayer player, int i, int j, int k, int meta, CallbackInfo ci) {
        CurseManager man = player.btj$getCurseManager();
        man.blockBrokenCallback(player, ((Block) (Object) this).blockID, new BlockPos(i, j, k), meta);
    }
}
