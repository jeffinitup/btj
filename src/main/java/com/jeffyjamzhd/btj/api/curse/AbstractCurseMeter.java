package com.jeffyjamzhd.btj.api.curse;

import com.jeffyjamzhd.btj.BetterThanJosh;
import net.minecraft.src.*;


public abstract class AbstractCurseMeter extends AbstractCurse {
    /**
     * Base texture BTJ curse meters use
     */
    public static final ResourceLocation CURSES_TEX = BetterThanJosh.idOf("textures/gui/curse/tex.png");

    /**
     * Count of tick updates (rolls back to 0 after reaching 100)
     */
    protected int updateCount = 0;

    /**
     * Value of this bar
     */
    protected int value;

    /**
     * Max value of this bar
     */
    protected int maxValue;

    public AbstractCurseMeter() {
        super();
    }

    @Override
    public void clientInit() {
        super.clientInit();
    }

    @Override
    public void onTick(World world, EntityPlayer player) {
        super.onTick(world, player);
        bumpUpdateCount();
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

    public int getValue() {
        return this.value;
    }

    public int getMaxValue() {
        return this.maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        this.value = maxValue;
    }

    public void setValue(int value) {
        this.value = Math.min(value, this.maxValue);
    }

    public void bumpUpdateCount() {
        this.updateCount += 1;
        if (this.updateCount >= 100) {
            this.dirty = true;
            this.updateCount = 0;
        }
    }

    public int getUpdateCount() {
        return this.updateCount;
    }
}
