package com.jeffyjamzhd.btj.api.curse;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

/**
 * Base interface for all curses.
 */
public interface ICurse {
    /**
     * Gets this curse's identifier
     */
    String getIdentifier();

    /**
     * Sets this curse's identifier (registry)
     */
    void setIdentifier(String id);

    /**
     * Runs on each tick that this curse is active
     */
    void onTick(World world, EntityPlayer player);

    /**
     * Runs upon the curse's activation
     */
    void onActivation(World world, EntityPlayer player);

    /**
     * Runs upon the curse's deactivation
     */
    void onDeactivation(World world, EntityPlayer player);

    /**
     * Writes to player's NBT
     */
    NBTTagCompound writeNBT(NBTTagCompound nbt);

    /**
     * Read from player's NBT
     */
    NBTTagCompound readNBT(NBTTagCompound nbt);
}
