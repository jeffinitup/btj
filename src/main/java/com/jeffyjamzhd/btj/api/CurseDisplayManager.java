package com.jeffyjamzhd.btj.api;

import com.jeffyjamzhd.btj.api.curse.ICurse;
import com.jeffyjamzhd.btj.api.curse.ICurseBar;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.Minecraft;
import net.minecraft.src.ResourceLocation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Manages the display of the client player's curses
 */
@Environment(EnvType.CLIENT)
public class CurseDisplayManager {
    private List<ICurseBar> sortedCurses = new ArrayList<>();

    /**
     * Renders all curse bars
     * @param player Player
     * @param left HUD left margin
     * @param right HUD right margin
     * @param height HUD base height
     */
    public void renderCurses(EntityPlayer player, GuiIngame gui, int left, int right, int height) {
        CurseManager man = player.btj$getCurseManager();
        List<ICurse> curses = man.getCurses();

        // Sort if necessary
        if (curses.size() != sortedCurses.size())
            sortCurses(curses);

        // Render curses
        int factor = 9;
        ResourceLocation last = null;
        for (ICurseBar curse : sortedCurses) {
            // Bind texture
            if (last != curse.getTexture())
                Minecraft.getMinecraft().getTextureManager().bindTexture(curse.getTexture());

            // Draw
            curse.drawBar(gui, right - 81, height - factor);
            factor += 12;
            last = curse.getTexture();
        }
    }

    /**
     * Renders the "curse get" banner
     * @param player
     */
    public void renderCurseGet(EntityPlayer player) {

    }

    /**
     * Sorts curses from highest to lowest render priority
     */
    private void sortCurses(List<ICurse> curses) {
        this.sortedCurses = curses.stream()
                .filter(iCurse -> iCurse instanceof ICurseBar)
                .map(iCurse -> (ICurseBar) iCurse)
                .sorted(Comparator.comparingInt((ICurseBar::getDisplayPriority)))
                .toList();
    }
}
