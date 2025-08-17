package com.jeffyjamzhd.btj.mixin.gui;

import com.jeffyjamzhd.btj.BetterThanJosh;
import com.jeffyjamzhd.btj.gui.credits.GuiCredits;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.io.IOException;
import java.text.MessageFormat;

@Mixin(GuiMainMenu.class)
@Environment(EnvType.CLIENT)
public abstract class GuiMainMenuMixin extends GuiScreen {
    @Unique
    private static final ResourceLocation titlePath = BetterThanJosh.idOf("textures/gui/logo.png");
    @Shadow
    private static final ResourceLocation splashTexts = BetterThanJosh.idOf("texts/splashes.txt");

    @ModifyArg(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/TextureManager;bindTexture(Lnet/minecraft/src/ResourceLocation;)V"))
    private ResourceLocation setTexture(ResourceLocation par1) {
        return titlePath;
    }

    @ModifyArgs(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiMainMenu;drawTexturedModalRect(IIIIII)V", ordinal = 5))
    private void drawTitleRect(Args args) {
        int centerX = this.width / 2;
        int width = 246;
        int height = 46;
        args.set(0, centerX - (width / 2));
        args.set(4, width);
        args.set(5, height);
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiMainMenu;drawTexturedModalRect(IIIIII)V", ordinal = 6))
    private void removeTitleRect(GuiMainMenu instance, int a, int b, int c, int d, int e, int f) {}

    @ModifyArg(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiMainMenu;drawString(Lnet/minecraft/src/FontRenderer;Ljava/lang/String;III)V", ordinal = 1), index = 1)
    private String modifyVersionDisplay(String par2) {
        return BetterThanJosh.getInstance().getPrettyVerisonString();
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;add(Ljava/lang/Object;)Z"))
    private <E> Object formatString(E e) {
        return MessageFormat.format((String) e, Minecraft.getMinecraft().getSession().getUsername());
    }

    @Inject(method = "addSingleplayerMultiplayerButtons", at = @At("TAIL"))
    private void addCreditsButton(int par1, int par2, CallbackInfo ci) {
        this.buttonList.add(new GuiButton(3000, this.width / 2 - 100, par1 + par2 * 2, I18n.getString("menu.credits")));
    }

    @Inject(method = "actionPerformed", at = @At("TAIL"))
    private void addCreditsFunction(GuiButton button, CallbackInfo ci) throws IOException {
        if (button.id == 3000)
            this.mc.displayGuiScreen(new GuiCredits(this));
    }
}
