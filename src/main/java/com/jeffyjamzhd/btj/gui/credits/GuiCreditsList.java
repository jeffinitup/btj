package com.jeffyjamzhd.btj.gui.credits;

import com.jeffyjamzhd.btj.BetterThanJosh;
import net.fabricmc.loader.impl.util.StringUtil;
import net.minecraft.src.*;
import org.spongepowered.include.com.google.common.base.Charsets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.jeffyjamzhd.btj.BetterThanJosh.LOGGER;

public class GuiCreditsList extends GuiSlot {
    private static ResourceLocation namesPath = BetterThanJosh.idOf("texts/signees.txt");

    private Minecraft mc;
    private GuiCredits credits;
    private FontRenderer fontRenderer;
    private List<String> names;

    public GuiCreditsList(GuiCredits credits, Minecraft mc, FontRenderer fr) throws IOException {
        super(mc, credits.width, credits.height, 64, credits.height - 40, fr.FONT_HEIGHT + 2);
        this.credits = credits;
        this.mc = mc;
        this.fontRenderer = fr;

        // Import credits
        BufferedReader reader = null;
        try {
            this.names = new ArrayList<>();
            InputStreamReader stream = new InputStreamReader(
                    this.mc.getResourceManager().getResource(namesPath).getInputStream(), Charsets.UTF_8);
            reader = new BufferedReader(stream);

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty())
                    names.add(line);
            }
        } catch (IOException exception) {
            LOGGER.error(exception);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException exception) {
                    LOGGER.error(exception);
                }
            }
        }

        names = names.stream().map(StringUtil::capitalize).sorted().toList();
    }

    @Override
    protected int getSize() {
        return this.names.size();
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
        int width = this.fontRenderer.getStringWidth(names.get(i));
        this.fontRenderer.drawStringWithShadow(names.get(i), this.credits.width / 2 - (width / 2), k, 0xFFFFFFFF);
    }

    @Override
    protected int getScrollBarX() {
        return this.credits.width - 6;
    }
}
