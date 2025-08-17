package com.jeffyjamzhd.btj.curse;

import com.jeffyjamzhd.btj.api.curse.AbstractCurseMeter;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.ResourceLocation;
import net.minecraft.src.World;

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
        gui.drawString(gui.btj$getFontRenderer(), "Test!", 70, 60,  0xFFFFFFFF);
        LOGGER.info("TEST");
    }
}
