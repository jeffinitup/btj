package com.jeffyjamzhd.btj.gui.credits;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class GuiCredits extends GuiScreen {
    protected GuiScreen parentScreen;
    protected String screenTitle;
    protected String[] screenDesc = new String[2];
    protected GuiCreditsList list;

    public GuiCredits(GuiScreen parent) {
        this.parentScreen = parent;
    }

    @Override
    public void initGui() {
        this.screenTitle = I18n.getString("menu.credits");
        this.screenDesc[0] = I18n.getString("menu.credits.desc1");
        this.screenDesc[1] = I18n.getString("menu.credits.desc2");
        try {
            this.list = new GuiCreditsList(this, this.mc, this.fontRenderer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.buttonList.add(new GuiButton(1, this.width / 2 - 150, this.height - 30, 300, 20, I18n.getString("menu.credits.back")));
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        this.drawDefaultBackground();
        this.list.drawScreen(i,j,f);
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 16, 0xFFFFFFFF);
        for (int x = 0; x < this.screenDesc.length; ++x)
            this.drawCenteredString(this.fontRenderer, EnumChatFormatting.ITALIC + this.screenDesc[x], this.width / 2, 32 + (12 * x), 0xFFAAAAAA);

        super.drawScreen(i, j, f);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1)
            this.mc.displayGuiScreen(this.parentScreen);
    }
}
