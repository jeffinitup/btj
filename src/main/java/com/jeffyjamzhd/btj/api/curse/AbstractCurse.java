package com.jeffyjamzhd.btj.api.curse;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;

import java.io.DataInputStream;

public class AbstractCurse implements ICurse {
    private String identifier;
    private String[] displayStrings;
    protected boolean dirty;

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public void setDisplayStrings(String[] strings) {
        this.displayStrings = strings;
    }

    @Override
    public int getAlignment() {
        return 0;
    }

    @Override
    public String[] getDisplayStrings() {
        return displayStrings;
    }

    @Override
    public void setIdentifier(String id) {
        this.identifier = id;
    }

    @Override
    public void onTick(World world, EntityPlayer player) {
        if (this.dirty && player instanceof EntityPlayerMP mp) {
            syncToClient(mp.playerNetServerHandler);
            this.dirty = false;
        }
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

    @Override
    public void syncToClient(NetServerHandler handler) {
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void parseSyncPacket(DataInputStream stream) {
    }

    @Override
    public ICurse createInstance() {
        return new AbstractCurse();
    }
}
