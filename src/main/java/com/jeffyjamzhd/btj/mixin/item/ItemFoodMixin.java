package com.jeffyjamzhd.btj.mixin.item;

import com.jeffyjamzhd.btj.api.CurseManager;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFood.class)
public class ItemFoodMixin {
    @Inject(method = "onEaten", at = @At("RETURN"))
    private void addCallback(
            ItemStack stack, World world, EntityPlayer player,
            CallbackInfoReturnable<ItemStack> cir) {
        CurseManager man = player.btj$getCurseManager();
        man.consumeFoodCallback(player, cir.getReturnValue());
    }
}
