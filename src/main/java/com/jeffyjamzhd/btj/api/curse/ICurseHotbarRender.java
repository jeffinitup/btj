package com.jeffyjamzhd.btj.api.curse;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.ResourceLocation;

/**
 * Curses that follow the traditional meter system (like the health and hunger bar)
 * should implement this interface
 */
public interface ICurseHotbarRender {
    int ALIGNMENT_LEFT = 0;
    int ALIGNMENT_RIGHT = 1;

    /**
     * Draw method for this curse
     */
    @Environment(EnvType.CLIENT)
    void drawBar(GuiIngame gui, int width, int height);

    /**
     * Update method for hotbar curses (e.g. shaking the pips or something)
     */
    @Environment(EnvType.CLIENT)
    void drawUpdateTick(GuiIngame gui);

    /**
     * Assigns rendering information
     * @param x Left render coordinate
     * @param y Top render coordinate
     * @param sizeX Right render coordinate
     * @param sizeY Bottom render coordinate
     */
    @Environment(EnvType.CLIENT)
    void setRenderLocation(int x, int y, int sizeX, int sizeY);

    /**
     * Set's this bar's texture
     */
    @Environment(EnvType.CLIENT)
    void setTexture(ResourceLocation texture);

    /**
     * Gets the texture of this curse's display
     */
    @Environment(EnvType.CLIENT)
    ResourceLocation getTexture();

    /**
     * Gets the priority of this curse's display in the HUD stack
     */
    int getDisplayPriority();


}
