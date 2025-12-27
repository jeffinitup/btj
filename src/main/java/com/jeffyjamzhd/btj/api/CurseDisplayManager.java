package com.jeffyjamzhd.btj.api;

import com.jeffyjamzhd.btj.api.curse.AbstractCurse;
import com.jeffyjamzhd.btj.api.curse.ICurseBar;
import com.jeffyjamzhd.btj.registry.BTJSound;
import com.jeffyjamzhd.btj.util.BTJMath;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Manages the display of the client player's curses
 */
@Environment(EnvType.CLIENT)
public class CurseDisplayManager {
    /**
     * Curses copied from client player, sorted by render priority
     */
    private List<ICurseBar> sortedCurses = new ArrayList<>();


    private byte contentsLeft = 0;
    private byte contentsRight = 0;

    private String[] curseStringsToRender;
    private long curseRenderTimer;
    private boolean playedCaveSound;

    /**
     * Renders all curse bars
     * @param player Player
     * @param gui GuiIngame
     * @param left HUD left margin
     * @param right HUD right margin
     * @param height HUD base height
     */
    public void renderCurses(EntityPlayer player, GuiIngame gui, int left, int right, int height) {
        CurseManager man = player.btj$getCurseManager();
        List<AbstractCurse> curses = man.getCurses();

        // Sort if necessary
        if (curses.size() != sortedCurses.size())
            sortCurses(curses);

        // Render curses
        int factor = 10;
        ResourceLocation last = null;
        for (ICurseBar curse : sortedCurses) {
            // Bind texture
            if (last != curse.getTexture())
                Minecraft.getMinecraft().getTextureManager().bindTexture(curse.getTexture());

            // Draw
            curse.drawBar(gui, right - 9, height - factor);
            factor += 10;
            last = curse.getTexture();
        }
    }

    /**
     * Handles curse display update tick
     * @param gui GuiIngame
     */
    public void renderCursesUpdateTick(GuiIngame gui) {
        if (sortedCurses.isEmpty())
            return;

        for (ICurseBar curse : sortedCurses) {
            curse.drawUpdateTick(gui);
        }
    }

    public void beginRenderingCurse(AbstractCurse curse) {
        Minecraft.getMinecraft().sndManager.playSoundFX(BTJSound.CURSE.sound(), 1.0F, 0.5F);
        this.curseStringsToRender = curse.getDisplayStrings();
        for (int i = 0; i < this.curseStringsToRender.length; i++)
            this.curseStringsToRender[i] = I18n.getString(this.curseStringsToRender[i]);
        this.curseRenderTimer = Minecraft.getSystemTime() + 10000L;
        this.playedCaveSound = false;
    }

    /**
     * Renders the "curse get" banner
     */
    public void renderCurseGet(GuiIngame gui, int width, int height) {
        if (this.curseStringsToRender != null && Minecraft.getSystemTime() < this.curseRenderTimer) {
            long time = (this.curseRenderTimer - Minecraft.getSystemTime());
            float range = time / 10000F;
            int length;

            float enterAnim = BTJMath.range(time / 1000F, 0F, 1F);
            float exitAnim = BTJMath.range((time - 9000F) / 1000F, 0F, 1F);
            float crossAnim = BTJMath.range(Math.max(0F, 5900F - time) / 1000F, 0F, 1F);
            float enter2Anim = BTJMath.range(Math.max(0F, 5100F - time) / 1000F, 0F, 1F);

            enterAnim = BTJMath.easeOutCubic(enterAnim - exitAnim);

            GL11.glPushMatrix();
            gui.drawGradientRect(0, 60 - (int)(25 * enterAnim), width, 60 + (int)(25 * enterAnim), 0x60000000, 0xA0000000);
            GL11.glEnable(GL11.GL_BLEND);
            if (crossAnim < 1F) {
                GL11.glScaled(2.0F, 2.0F, 2.0F);
                GL11.glTranslatef(-width / 4F, -24F, 0F);
                length = gui.btj$getFontRenderer().getStringWidth(this.curseStringsToRender[0]);
                gui.drawString(gui.btj$getFontRenderer(), this.curseStringsToRender[0], width / 2 - (length / 2), 50, 0xFFFFFF + ((int) (2F + ((enterAnim * 252F) - (crossAnim * 250F))) << 24));
            }
            if (crossAnim >= 1F) {
                if (!this.playedCaveSound) {
                    Minecraft.getMinecraft().sndManager.playSoundFX("ambient.cave.cave9", 0.8F, 0.7F);
                    this.playedCaveSound = true;
                }
                length = gui.btj$getFontRenderer().getStringWidth(this.curseStringsToRender[1]);
                gui.drawString(gui.btj$getFontRenderer(), this.curseStringsToRender[1], width / 2 - (length / 2), 55, 0xFFFFFF + ((int) (4F + ((enter2Anim * 252F) + (enterAnim * 250F))) << 24));
            }

            GL11.glPopMatrix();
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    /**
     * Sorts curses from highest to lowest render priority
     */
    private void sortCurses(List<AbstractCurse> curses) {
        this.contentsLeft = 0;
        this.contentsRight = 0;
        this.sortedCurses = curses.stream()
                .filter(AbstractCurse -> AbstractCurse instanceof ICurseBar)
                .map(AbstractCurse -> (ICurseBar) AbstractCurse)
                .sorted(Comparator.comparingInt((ICurseBar::getDisplayPriority)))
                .toList();
        this.sortedCurses.forEach(curseBar -> this.contentsRight++);
    }

    public int getOffsetYLeft() {
        return (int) this.contentsLeft * 10;
    }

    public int getOffsetYRight() {
        return (int) this.contentsRight * 10;
    }
}
