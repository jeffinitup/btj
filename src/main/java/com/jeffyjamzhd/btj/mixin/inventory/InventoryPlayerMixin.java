package com.jeffyjamzhd.btj.mixin.inventory;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.src.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InventoryPlayer.class)
public class InventoryPlayerMixin {
    /**
     * Set maximum stack size 64 -> 67
     */
    @ModifyReturnValue(method = "getInventoryStackLimit", at = @At("RETURN"))
    private int to67(int original) {
        return 65000;
    }
}
