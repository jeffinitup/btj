package com.jeffyjamzhd.btj.mixin.gui;

import com.jeffyjamzhd.btj.api.CurseDisplayManager;
import com.jeffyjamzhd.btj.api.extend.IGuiIngame;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(GuiIngame.class)
public abstract class GuiIngameMixin extends Gui implements IGuiIngame {
    @Shadow @Final private Minecraft mc;
    @Unique private CurseDisplayManager curse_dm;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void createDM(Minecraft par1Minecraft, CallbackInfo ci) {
        curse_dm = new CurseDisplayManager();
    }

    @Inject(method = "func_110327_a", at = @At("TAIL"))
    private void renderCallbackCurses(
            int par1, int par2, CallbackInfo ci,
            @Local(name = "var11") int left,
            @Local(name = "var12") int right,
            @Local(name = "var13") int height) {
        this.curse_dm.renderCurseGeneric(this.mc.thePlayer, (GuiIngame) (Object) this);
        this.curse_dm.renderCurseBars(this.mc.thePlayer, (GuiIngame) (Object) this, left, right, height);
    }

    @Inject(method = "updateTick", at = @At("TAIL"))
    private void renderCallbackCurseUpdateTick(CallbackInfo ci) {
        this.curse_dm.renderCursesUpdateTick(((GuiIngame) (Object) this));
    }

    @Inject(method = "renderGameOverlay", at = @At("TAIL"))
    private void renderCallbackCursesGet(
            float par1, boolean par2, int par3, int par4, CallbackInfo ci,
            @Local(name = "var6") int width,
            @Local(name = "var7") int height) {
        this.curse_dm.renderCurseGet(((GuiIngame) (Object) this), width, height);
    }

    @Inject(method = "func_110327_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityClientPlayerMP;getAir()I", ordinal = 1))
    private void addOffsetToAir(int par1, int par2, CallbackInfo ci,
                                @Local(name = "var18") LocalIntRef offset) {
        int y = offset.get();
        offset.set(y - this.curse_dm.getOffsetYRight());
    }

    @ModifyArg(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/FontRenderer;drawStringWithShadow(Ljava/lang/String;III)I", ordinal = 0), index = 2)
    private int shiftItemDisplay(int par2) {
        return par2
                - Math.max(this.curse_dm.getOffsetYLeft(), this.curse_dm.getOffsetYRight())
                - (this.mc.thePlayer.getAllActiveStatusEffects().size() * 10)
                - (this.mc.thePlayer.getAir() < 300 || this.mc.thePlayer.isInsideOfMaterial(Material.water) ? 10 : 0);
    }

    @ModifyArg(method = "drawPenaltyText", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/FontRenderer;drawStringWithShadow(Ljava/lang/String;III)I"), index = 2)
    private int shiftPenaltyDisplay(int par2) {

        return par2 - this.curse_dm.getOffsetYRight() +
                (this.mc.thePlayer.getAir() < 300 || this.mc.thePlayer.isInsideOfMaterial(Material.water) ? this.curse_dm.getOffsetYRight() : 0);
    }

    @Override
    public CurseDisplayManager btj$getCurseDisplay() {
        return this.curse_dm;
    }

    @Override
    public FontRenderer btj$getFontRenderer() {
        return this.mc.fontRenderer;
    }
}
