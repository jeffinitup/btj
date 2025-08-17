package com.jeffyjamzhd.btj.mixin.gui;

import com.jeffyjamzhd.btj.api.CurseDisplayManager;
import com.jeffyjamzhd.btj.api.extend.IGuiIngame;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(GuiIngame.class)
public abstract class GuiIngameMixin extends Gui implements IGuiIngame {
    @Shadow @Final private Minecraft mc;
    @Unique
    private CurseDisplayManager curse_dm;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void createDM(Minecraft par1Minecraft, CallbackInfo ci) {
        curse_dm = new CurseDisplayManager();
    }

    @Inject(method = "func_110327_a", at = @At("TAIL"))
    private void renderCallback(int par1, int par2, CallbackInfo ci) {
        this.curse_dm.renderCurses(this.mc.thePlayer, ((GuiIngame) (Object) this), par1, par2);
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
