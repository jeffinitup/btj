package com.jeffyjamzhd.btj.mixin.item;

import net.minecraft.src.ItemFlintAndSteel;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.minecraft.src.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFlintAndSteel.class)
public class ItemFlintAndSteelMixin {

    @Inject(method = "onUpdate", at = @At("HEAD"))
    private void keepDurability(ItemStack stack, World world, Entity entity, int slot, boolean isHeld, CallbackInfo ci) {
        if (stack.getItemDamage() != 0) {
            stack.setItemDamage(0);
        }
    }
}

