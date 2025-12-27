package com.jeffyjamzhd.btj.mixin.item;

import com.jeffyjamzhd.btj.api.CurseManager;
import com.jeffyjamzhd.btj.item.BowlItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    /**
     * Set maximum stack size 64 -> 67
     */
    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 64))
    private int modifyMaxStackSize(int value) {
        return 65000;
    }

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

    /**
     * Replaces bowl item with new class {@link BowlItem}
     */
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(I)Lnet/minecraft/src/Item;", ordinal = 3))
    private static Item changeBowl(int i) {
        return new BowlItem(i);
    }
}
