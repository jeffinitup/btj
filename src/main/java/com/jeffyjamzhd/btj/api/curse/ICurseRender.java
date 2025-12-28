package com.jeffyjamzhd.btj.api.curse;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.GuiIngame;

/**
 * Curses that require some kind of generic draw functionality when they are active
 */
public interface ICurseRender {
    /**
     * Draw method
     * @param gui GUI
     */
    @Environment(EnvType.CLIENT)
    default void draw(GuiIngame gui) {}
}
