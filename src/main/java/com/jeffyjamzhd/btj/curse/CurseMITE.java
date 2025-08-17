package com.jeffyjamzhd.btj.curse;

import com.jeffyjamzhd.btj.api.curse.AbstractCurseMeter;
import net.minecraft.src.*;

import static com.jeffyjamzhd.btj.BetterThanJosh.LOGGER;

/**
 * Curse of MITE, must eat seeds!
 */
public class CurseMITE extends AbstractCurseMeter {
    public CurseMITE(ResourceLocation texture) {
        super(texture);
    }

    @Override
    public void onTick(World world, EntityPlayer player) {
        super.onTick(world, player);
    }

    @Override
    public void drawBar(GuiIngame gui, int width, int height) {
        gui.drawTexturedModalRect(width, height, 0, 8, 91, 8);
    }
}
