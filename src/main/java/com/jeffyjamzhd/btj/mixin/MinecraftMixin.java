package com.jeffyjamzhd.btj.mixin;

import com.jeffyjamzhd.btj.BetterThanJosh;
import com.jeffyjamzhd.btj.registry.BTJCurses;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow public EntityClientPlayerMP thePlayer;

    /**
     * Set window name to the TRUE one.
     */
    @ModifyArg(method = "startGame", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V"), remap = false)
    private String setTitleToJosh(String newTitle) {
        return BetterThanJosh.getInstance().getPrettyVerisonString();
    }

    /**
     * Update for CurseFPS
     * @see com.jeffyjamzhd.btj.curse.CurseFPS
     */
    @Inject(method = "getLimitFramerate", at = @At(value = "RETURN"), cancellable = true)
    private void syncWhenCursed(CallbackInfoReturnable<Integer> cir) {
        if (this.thePlayer != null && this.thePlayer.btj$getCurseManager().hasCurse(BTJCurses.CURSE_FPS))
            cir.setReturnValue(1);
    }
}
