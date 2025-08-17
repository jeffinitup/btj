package com.jeffyjamzhd.btj.mixin;

import com.jeffyjamzhd.btj.BetterThanJosh;
import net.minecraft.src.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    /**
     * Set window name to the TRUE one.
     */
    @ModifyArg(method = "startGame", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V"))
    private String setTitleToJosh(String newTitle) {
        return BetterThanJosh.getInstance().getPrettyVerisonString();
    }
}
