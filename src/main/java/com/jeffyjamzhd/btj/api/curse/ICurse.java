package com.jeffyjamzhd.btj.api.curse;

import com.jeffyjamzhd.btj.api.CurseRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.World;

import java.io.DataInputStream;

/**
 * Base interface for all curses.
 */
public interface ICurse {
    /**
     * Gets this curse's identifier
     */
    String getIdentifier();

    /**
     * Gets this curse's display string(s)
     * @return Display strings
     */
    String[] getDisplayStrings();

    /**
     * Gets this curse's alignment on screen
     */

    int getAlignment();

    /**
     * Sets this curse's display string(s)
     */
    void setDisplayStrings(String[] strings);

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
     * Writes to curse's NBT tag compound
     */
    NBTTagCompound writeNBT(NBTTagCompound nbt);

    /**
     * Read from curse's NBT tag compound
     */
    NBTTagCompound readNBT(NBTTagCompound nbt);

    /**
     * Sends a sync packet to client (Serverside)
     * @param handler Network handler
     */
    void syncToClient(NetServerHandler handler);

    /**
     * Handles syncing packet (Clientside)
     * @param stream Data stream
     */
    @Environment(EnvType.CLIENT)
    void parseSyncPacket(DataInputStream stream);

    /**
     * Initializes the curse (common side)
     */
    void init();

    /**
     * Initializes the curse (client side) (e.g. gui code)
     */
    @Environment(EnvType.CLIENT)
    void clientInit();

    /**
     * Creates an instance of this curse
     */
    ICurse createInstance();
}
