package com.jeffyjamzhd.btj.api;

import com.jeffyjamzhd.btj.api.curse.ICurse;
import com.jeffyjamzhd.btj.api.curse.ICurseBar;
import com.jeffyjamzhd.btj.api.extend.IEntityPlayer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiIngame;

import java.util.List;

/**
 * Manages the display of the client player's curses
 */
@Environment(EnvType.CLIENT)
public class CurseDisplayManager {
    /**
     * Renders all curse bars
     * @param player Player
     * @param width HUD width
     * @param height HUD height
     */
    public void renderCurses(EntityPlayer player, GuiIngame gui, int width, int height) {
        CurseManager man = player.btj$getCurseManager();
        List<ICurse> curses = man.getCurses();
        for (ICurse curse : curses) {
            if (curse instanceof ICurseBar curseBar) {
                curseBar.drawBar(gui, width, height);
            }
        }
    }

    /**
     * Renders the "curse get" banner
     * @param player
     */
    public void renderCurseGet(EntityPlayer player) {

    }
}
