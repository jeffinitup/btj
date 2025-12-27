package com.jeffyjamzhd.btj.mixin.item;

import btw.item.BTWItems;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemNameMixin {

    @Inject(method = "getUnlocalizedName(Lnet/minecraft/src/ItemStack;)Ljava/lang/String;",
            at = @At("HEAD"), cancellable = true)
    private void replaceNames(ItemStack stack, CallbackInfoReturnable<String> cir) {

        Item item = stack.getItem();

        if (item == BTWItems.firePlough) {
            cir.setReturnValue("item.btj.FlintandLooseRock");
        }

        if (item == BTWItems.bowDrill) {
            cir.setReturnValue("item.btj.FlintandFlint");
        }

        if (item == Item.flintAndSteel) {
            cir.setReturnValue("item.btj.FlintandSoulforgedSteelNugget");
        }
    }
}
