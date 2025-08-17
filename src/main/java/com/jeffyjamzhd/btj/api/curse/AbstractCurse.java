package com.jeffyjamzhd.btj.api.curse;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class AbstractCurse implements ICurse {
    public String identifier;

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public void setIdentifier(String id) {
        this.identifier = id;
    }

    @Override
    public void onTick(World world, EntityPlayer player) {
    }

    @Override
    public void onActivation(World world, EntityPlayer player) {
    }

    @Override
    public void onDeactivation(World world, EntityPlayer player) {
    }

    @Override
    public NBTTagCompound writeNBT(NBTTagCompound nbt) {
        return nbt;
    }

    @Override
    public NBTTagCompound readNBT(NBTTagCompound nbt) {
        return nbt;
    }
}
