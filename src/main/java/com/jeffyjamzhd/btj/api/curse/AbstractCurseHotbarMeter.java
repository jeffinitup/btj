package com.jeffyjamzhd.btj.api.curse;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.ResourceLocation;

import java.util.Random;

public abstract class AbstractCurseHotbarMeter extends AbstractCurseMeter implements ICurseHotbarRender {
    /**
     * Draw priority
     */
    protected int priority = 100;

    /**
     * Chance for each pip to shake
     */
    @Environment(EnvType.CLIENT)
    protected float shakeChance = 0F;

    /**
     * Ticks to shake pips for
     */
    @Environment(EnvType.CLIENT)
    protected int shakeTicks = 0;

    /**
     * Offsets to apply to pips. Not to be directly modified
     */
    @Environment(EnvType.CLIENT)
    private final int[] shakeOffsets = new int[10];

    /**
     * Texture location
     */
    @Environment(EnvType.CLIENT)
    private ResourceLocation texture;

    /**
     * Texture rendering information
     */
    @Environment(EnvType.CLIENT)
    private int textureX;
    @Environment(EnvType.CLIENT)
    private int textureY;
    @Environment(EnvType.CLIENT)
    private int sizeX;
    @Environment(EnvType.CLIENT)
    private int sizeY;

    @Override
    public void clientInit() {
        super.clientInit();
        this.setTexture(CURSES_TEX);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawBar(GuiIngame gui, int width, int height) {
        int pipBound = this.getMaxValue() / 10;

        for (int i = 0; i <= 9; i++) {
            // Get amount
            int barAmount = Math.max(this.value - (pipBound * i), 0);
            if (barAmount > pipBound)
                barAmount = pipBound;

            // Draw
            gui.drawTexturedModalRect(width - (i * 8), height + this.shakeOffsets[i], textureX, textureY, sizeX, sizeY);
            if (barAmount > 0) {
                int offsetX = (int) Math.ceil(9F * (barAmount / (float) pipBound));
                gui.drawTexturedModalRect(
                        width - (i * 8) + (9 - offsetX), height + this.shakeOffsets[i],
                        textureX + sizeX - offsetX, sizeY,
                        offsetX, sizeY);
            }
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawUpdateTick(GuiIngame gui) {
        Random rand = gui.btj$getFontRenderer().fontRandom;
        for (int i = 0; i <= 9; i++) {
            // Force shake if ticks over 0
            if (this.shakeTicks > 0) {
                this.shakeOffsets[i] = rand.nextInt(3) - 1;
                continue;
            }

            // Otherwise random chance
            int shakeY = rand.nextFloat() < this.shakeChance ? rand.nextInt(3) - 1 : 0;
            this.shakeOffsets[i] = shakeY;
        }

        // Decrement shake ticks if over 0
        this.shakeTicks = this.shakeTicks > 0 ? this.shakeTicks - 1 : this.shakeTicks;
    }

    /**
     * Assigns rendering information
     * @param x Left render coordinate
     * @param y Top render coordinate
     * @param sizeX Right render coordinate
     * @param sizeY Bottom render coordinate
     */
    @Environment(EnvType.CLIENT)
    public void setRenderLocation(int x, int y, int sizeX, int sizeY) {
        this.textureX = x;
        this.textureY = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    @Override
    public int getDisplayPriority() {
        return this.priority;
    }

    @Override
    public void setTexture(ResourceLocation texture) {
        this.texture = texture;
    }

    @Override
    public ResourceLocation getTexture() {
        return this.texture;
    }

    public void setShakeTicks(int ticks) {
        this.shakeTicks = Math.max(0, ticks);
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
