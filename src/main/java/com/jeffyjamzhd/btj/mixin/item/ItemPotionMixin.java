package com.jeffyjamzhd.btj.mixin.item;

import com.jeffyjamzhd.btj.api.CurseManager;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemPotion;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemPotion.class)
public class ItemPotionMixin {
    /**
     * Eat callback event
     */
    @Inject(method = "onEaten", at = @At("RETURN"))
    private void eatCallback(
            ItemStack stack, World world, EntityPlayer player,
            CallbackInfoReturnable<ItemStack> cir) {
        CurseManager man = player.btj$getCurseManager();
        man.consumeFoodCallback(player, cir.getReturnValue());
    }
}
