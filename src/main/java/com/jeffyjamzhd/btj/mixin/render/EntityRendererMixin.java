package com.jeffyjamzhd.btj.mixin.render;

import com.jeffyjamzhd.btj.registry.BTJCurses;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
@Environment(EnvType.CLIENT)
public class EntityRendererMixin {
    @Shadow private long renderEndNanoTime;

    @Inject(method = "performanceToFps", at = @At("HEAD"), cancellable = true)
    private static void curseFPSMode(int par0, CallbackInfoReturnable<Integer> cir) {
        if (Minecraft.getMinecraft().thePlayer != null &&
                Minecraft.getMinecraft().thePlayer.btj$getCurseManager().hasCurse(BTJCurses.CURSE_FPS))
            cir.setReturnValue(24);
    }

    @ModifyArg(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityRenderer;renderWorld(FJ)V", ordinal = 0), index = 1)
    private long forceCurseFPS(long par2) {
        if (Minecraft.getMinecraft().thePlayer != null &&
                Minecraft.getMinecraft().thePlayer.btj$getCurseManager().hasCurse(BTJCurses.CURSE_FPS)) {
            return this.renderEndNanoTime + (1000000000L / 24);
        }
        return par2;
    }
}
