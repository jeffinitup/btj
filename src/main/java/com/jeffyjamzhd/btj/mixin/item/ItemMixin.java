package com.jeffyjamzhd.btj.mixin.item;

import net.minecraft.src.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Item.class)
public class ItemMixin {
    /**
     * Set maximum stack size 64 -> 67
     */
    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 64))
    private int modifyMaxStackSize(int value) {
        return 65000;
    }
}
