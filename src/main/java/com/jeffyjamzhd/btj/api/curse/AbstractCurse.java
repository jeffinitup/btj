package com.jeffyjamzhd.btj.api.curse;

import com.jeffyjamzhd.btj.api.CurseRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.src.*;

import java.io.DataInputStream;

public class AbstractCurse implements ICurse {
    /**
     * Identifier tied to this curse
     */
    private String identifier;

    /**
     * Curse display strings (the things that show up in the curse banner)
     */
    @Environment(EnvType.CLIENT)
    private String[] displayStrings;

    /**
     * Whether this curse is marked for an update
     */
    protected boolean dirty;

    public AbstractCurse() {
        this.dirty = true;
        this.init();
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            this.clientInit();
        }
    }

    @Override
    public void init() {
        this.identifier = CurseRegistry.INSTANCE.getIdentifier(this.getClass());
    }

    @Override
    public void clientInit() {
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void setDisplayStrings(String[] strings) {
        this.displayStrings = strings;
    }

    @Override
    public int getAlignment() {
        return 0;
    }

    @Override
    @Environment(EnvType.CLIENT)
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
            this.syncToClient(mp.playerNetServerHandler);
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
