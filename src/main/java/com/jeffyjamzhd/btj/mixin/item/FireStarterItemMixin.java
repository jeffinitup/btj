package com.jeffyjamzhd.btj.mixin.item;

import api.item.items.FireStarterItem;
import btw.item.items.FlintAndSteelItem;
import net.minecraft.src.EntityLivingBase;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FireStarterItem.class)
public class FireStarterItemMixin {
    @Redirect(method = "onItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ItemStack;damageItem(ILnet/minecraft/src/EntityLivingBase;)V"))
    private void doNotDamageFlintAndSteel(ItemStack instance, int i, EntityLivingBase par1) {
        if (instance.getItem() instanceof FlintAndSteelItem) {
            return;
        }
        instance.damageItem(1, par1);
    }
}
