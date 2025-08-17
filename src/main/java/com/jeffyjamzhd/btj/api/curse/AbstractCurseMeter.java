package com.jeffyjamzhd.btj.api.curse;

import net.minecraft.src.GuiIngame;
import net.minecraft.src.ResourceLocation;

public class AbstractCurseMeter extends AbstractCurse implements ICurseBar {
    private int priority = 100;
    private int value = 0;
    private int maxValue = 10000;

    private final ResourceLocation texture;

    public AbstractCurseMeter(ResourceLocation texture) {
        this.texture = texture;
    }

    @Override
    public ResourceLocation getTexture() {
        return this.texture;
    }

    @Override
    public void drawBar(GuiIngame gui, int width, int height) {

    }

    @Override
    public int getDisplayPriority() {
        return this.priority;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    public int getMaxValue() {
        return this.maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
