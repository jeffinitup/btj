package com.jeffyjamzhd.btj.api.curse;

import com.jeffyjamzhd.btj.BetterThanJosh;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;

import java.util.Random;

public class AbstractCurseMeter extends AbstractCurse implements ICurseBar {
    /**
     * Base texture BTJ curse meters use
     */
    private static final ResourceLocation CURSES_TEX = BetterThanJosh.idOf("textures/gui/curse/tex.png");

    /**
     * Draw priority
     */
    protected int priority = 100;

    /**
     * Count of tick updates (rolls back to 0 after reaching 100)
     */
    protected int updateCount = 0;

    /**
     * Value of this bar
     */
    protected int value = 0;

    /**
     * Max value of this bar
     */
    protected int maxValue = 10000;

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

    /**
     * Chance for this each pip to shake
     */
    @Environment(EnvType.CLIENT)
    protected float shakeChance = 0F;

    /**
     * Offsets to apply to pips. Not to be directly modified
     */
    @Environment(EnvType.CLIENT)
    private final int[] shakeOffsets = new int[10];

    public AbstractCurseMeter() {
        super();
    }

    @Override
    public void clientInit() {
        super.clientInit();
        this.setTexture(CURSES_TEX);
    }

    @Override
    public void onTick(World world, EntityPlayer player) {
        super.onTick(world, player);
        bumpUpdateCount();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ResourceLocation getTexture() {
        return this.texture;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void setTexture(ResourceLocation texture) {
        this.texture = texture;
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
        for (int i = 0; i <= 9; i++)
            this.shakeOffsets[i] = rand.nextFloat() < this.shakeChance ? rand.nextInt(3) - 1 : 0;
    }

    @Override
    public NBTTagCompound writeNBT(NBTTagCompound nbt) {
        nbt.setByte("UpdateCount", (byte) updateCount);
        return nbt;
    }

    @Override
    public NBTTagCompound readNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("UpdateCount"))
            this.updateCount = nbt.getByte("UpdateCount");
        return nbt;
    }

    @Override
    public int getDisplayPriority() {
        return this.priority;
    }

    @Override
    public int getValue() {
        return this.value;
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

    public int getMaxValue() {
        return this.maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        this.value = maxValue;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setValue(int value) {
        this.value = Math.min(value, this.maxValue);
    }

    public void bumpUpdateCount() {
        this.updateCount += 1;
        this.updateCount = this.updateCount > 100 ? 0 : this.updateCount;
    }

    public int getUpdateCount() {
        return this.updateCount;
    }

    @Override
    public ICurse createInstance() {
        return new AbstractCurseMeter();
    }
}
