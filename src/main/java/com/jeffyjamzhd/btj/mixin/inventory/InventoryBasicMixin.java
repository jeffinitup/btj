package com.jeffyjamzhd.btj.mixin.inventory;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.src.InventoryBasic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InventoryBasic.class)
public class InventoryBasicMixin {
    /**
     * Set maximum stack size 64 -> 67
     */
    @ModifyReturnValue(method = "getInventoryStackLimit", at = @At("RETURN"))
    private int to67(int original) {
        return 65000;
    }
}
