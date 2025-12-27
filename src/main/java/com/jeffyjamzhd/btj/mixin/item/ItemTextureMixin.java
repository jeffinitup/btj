package com.jeffyjamzhd.btj.mixin.item;

import btw.item.BTWItems;
import net.minecraft.src.Item;
import net.minecraft.src.ItemFlintAndSteel;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemTextureMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void overrideTextures(int id, CallbackInfo ci) {
        Item self = (Item)(Object)this;

        if (self == BTWItems.firePlough) {
            self.setTextureName("btj:FlintandLooseRock");
        }

        if (self == BTWItems.bowDrill) {
            self.setTextureName("btj:FlintandFlint");
        }

        if (self instanceof ItemFlintAndSteel) {
            self.setTextureName("btj:FlintandSoulforgedSteelNugget");
        }
    }
}
