package com.jeffyjamzhd.btj.gui.credits;

import com.jeffyjamzhd.btj.BetterThanJosh;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiCreditsList extends GuiSlot {
    private static final List<CreditsEntry> entries = new ArrayList<>();

    private final GuiCredits credits;
    private final FontRenderer fontRenderer;

    public GuiCreditsList(GuiCredits credits, Minecraft mc, FontRenderer fr) {
        super(mc, credits.width, credits.height, 64, credits.height - 40, 52);
        this.credits = credits;
        this.fontRenderer = fr;
    }

    @Override
    protected int getSize() {
        return entries.size();
    }

    @Override
    protected void elementClicked(int i, boolean bl) {
    }

    @Override
    protected boolean isSelected(int i) {
        return false;
    }

    @Override
    protected void drawBackground() {
    }

    @Override
    protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
        TextureManager man = Minecraft.getMinecraft().getTextureManager();
        CreditsEntry entry = entries.get(i); 
        man.bindTexture(entry.texture);

        // Draw icon of entry
        GL11.glColor4f(1F, 1F, 1F, 1F);
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(0xFFFFFF);
        tessellator.addVertexWithUV(j, (k + 48), 0F, 0F, 1F);
        tessellator.addVertexWithUV((j + 48), (k + 48), 0F, 1F, 1F);
        tessellator.addVertexWithUV((j + 48), k, 0F, 1F, 0F);
        tessellator.addVertexWithUV(j, k, 0F, 0F, 0F);
        tessellator.draw();

        // Draw entry text
        String[] desc = I18n.getString(entry.desc).split("&&");
        int offset = 0;

        this.fontRenderer.drawStringWithShadow(I18n.getString(entry.name), j + 52, k + 1, 0xFFFFFF);
        for (String string : desc) {
            this.fontRenderer.drawStringWithShadow(string, j + 52, k + 12 + (10 * offset), 0x888888);
            offset++;
        }
    }

    @Override
    protected int getScrollBarX() {
        return this.credits.width - 6;
    }

    record CreditsEntry(ResourceLocation texture, String name, String desc) {}

    static {
        // Define credits entries here
        CreditsEntry jeff = new CreditsEntry(BetterThanJosh.idOf("textures/gui/credits/jeff.png"), "credits.jeff.name", "credits.jeff.desc");
        CreditsEntry franzy = new CreditsEntry(BetterThanJosh.idOf("textures/gui/credits/franzy.png"), "credits.franzy.name", "credits.franzy.desc");
        CreditsEntry kitty = new CreditsEntry(BetterThanJosh.idOf("textures/gui/credits/kitty.png"), "credits.kitty.name", "credits.kitty.desc");

        entries.addAll(List.of(jeff, franzy, kitty));
    }
}
