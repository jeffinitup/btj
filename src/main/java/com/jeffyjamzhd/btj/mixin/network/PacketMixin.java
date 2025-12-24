package com.jeffyjamzhd.btj.mixin.network;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@Mixin(Packet.class)
public class PacketMixin {
    @Redirect(method = "writeItemStack", at = @At(value = "INVOKE", target = "Ljava/io/DataOutput;writeByte(I)V", ordinal = 0))
    private static void onWriteItemStack(DataOutput out, int value) throws IOException {
        out.writeShort(value);
    }

    @Inject(method = "readItemStack", at = @At(value = "INVOKE", target = "Ljava/io/DataInput;readByte()B", ordinal = 0), cancellable = true)
    private static void onReadItemStack(DataInput dataInput,
                                        CallbackInfoReturnable<ItemStack> cir,
                                        @Local(ordinal = 0) short item_id) throws IOException {
        short count = dataInput.readShort();
        short damage = dataInput.readShort();
        ItemStack stack = new ItemStack(item_id, count, damage);
        stack.stackTagCompound = Packet.readNBTTagCompound(dataInput);
        cir.setReturnValue(stack);
    }
}
