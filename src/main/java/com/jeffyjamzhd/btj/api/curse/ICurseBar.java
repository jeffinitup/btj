package com.jeffyjamzhd.btj.api.curse;

import net.minecraft.src.GuiIngame;
import net.minecraft.src.ResourceLocation;

/**
 * Curses that follow the traditional meter system (like the health and hunger bar)
 * should implement this interface
 */

public interface ICurseBar {
    /**
     * Gets the texture of this curse's display
     */
    ResourceLocation getTexture();

    /**
     * Draw method for this curse
     */
    void drawBar(GuiIngame gui, int width, int height);

    /**
     * Gets the priority of this curse's display in the HUD stack
     */
    int getDisplayPriority();

    /**
     * Returns the value of this curse's meter
     */
    int getValue();
}
