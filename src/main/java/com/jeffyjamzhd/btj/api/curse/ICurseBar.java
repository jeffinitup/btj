package com.jeffyjamzhd.btj.api.curse;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.ResourceLocation;

/**
 * Curses that follow the traditional meter system (like the health and hunger bar)
 * should implement this interface
 */

public interface ICurseBar {
    int ALIGNMENT_LEFT = 0;
    int ALIGNMENT_RIGHT = 1;

    /**
     * Gets the texture of this curse's display
     */
    ResourceLocation getTexture();

    /**
     * Set's this bar's texture
     */
    void setTexture(ResourceLocation texture);

    /**
     * Draw method for this curse
     */
    @Environment(EnvType.CLIENT)
    void drawBar(GuiIngame gui, int width, int height);

    /**
     * Update tick for gui
     */
    @Environment(EnvType.CLIENT)
    void drawUpdateTick(GuiIngame gui);

    /**
     * Gets the priority of this curse's display in the HUD stack
     */
    int getDisplayPriority();

    /**
     * Returns the value of this curse's meter
     */
    int getValue();
}
