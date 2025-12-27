package com.jeffyjamzhd.btj.api.curse;

import com.jeffyjamzhd.btj.api.CurseRegistry;
import com.jeffyjamzhd.btj.registry.BTJPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.src.*;
import org.jetbrains.annotations.ApiStatus;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static com.jeffyjamzhd.btj.BetterThanJosh.LOGGER;

public abstract class AbstractCurse {
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
        this.init();
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            this.clientInit();
        }
        this.dirty = true;
    }

    /**
     * Initializes the curse (common side)
     */
    public void init() {
        identifier = CurseRegistry.INSTANCE.getIdentifier(this.getClass());
    }

    /**
     * Initializes the curse (client side) (e.g. gui code)
     */
    @Environment(EnvType.CLIENT)
    public void clientInit() {
    }

    /**
     * Called every server tick
     * @param world World
     * @param player Player this curse is associated with
     */
    public void onTick(World world, EntityPlayer player) {
        if (this.dirty && player instanceof EntityPlayerMP mp) {
            // Sync when marked dirty
            this.syncToClientWrapper(mp.playerNetServerHandler);
            this.dirty = false;
        }
    }

    /**
     * Called upon the curse's activation
     */
    public void onActivation(World world, EntityPlayer player) {
    }

    /**
     * Runs upon the curse's deactivation
     */
    public void onDeactivation(World world, EntityPlayer player) {
    }

    /**
     * Writes to curse's NBT tag compound
     */
    public NBTTagCompound writeNBT(NBTTagCompound nbt) {
        return nbt;
    }

    /**
     * Read from curse's NBT tag compound
     */
    public NBTTagCompound readNBT(NBTTagCompound nbt) {
        return nbt;
    }

    //==============================================================================
    // NETCODE
    //==============================================================================

    /**
     * {@code true} if this curse should use the default packet system.
     * If {@code false}, you will need your own packet handling code
     */
    public boolean usePacketSystem() {
        return true;
    }

    /**
     * Returns this curse's packet identifier (e.g. {@code btj|curseMiTE})
     */
    public String getPacketIdentifier() {
        return "";
    }

    /**
     * Wrapper for basic S2C sync handling
     * @param handler Server handler
     */
    @ApiStatus.Internal
    public final void syncToClientWrapper(NetServerHandler handler) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        try {
            this.syncToClient(dataStream);
        } catch (IOException e) {
            LOGGER.trace(e);
        }

        byte[] data = byteStream.toByteArray();
        Packet250CustomPayload packet = new Packet250CustomPayload(this.getPacketIdentifier(), data);
        handler.sendPacketToPlayer(packet);
    }

    /**
     * Sends a sync packet to client (Serverside)
     * @param stream Outbound data stream
     */
    public void syncToClient(DataOutputStream stream) throws IOException {
    }

    /**
     * Wrapper for handling packet sent from the server (client sided)
     * @param stream Incoming data stream
     */
    @ApiStatus.Internal
    @Environment(EnvType.CLIENT)
    public final void syncFromServerWrapper(DataInputStream stream) {
        try {
            this.syncFromServer(stream);
        } catch (IOException e) {
            LOGGER.trace(e);
        }
    }

    /**
     * Handles data sent from the server (client sided)
     * @param stream Incoming data stream
     */
    @Environment(EnvType.CLIENT)
    public void syncFromServer(DataInputStream stream) throws IOException {
    }

    //==============================================================================
    // SET-GET
    //==============================================================================

    public String getIdentifier() {
        return this.identifier;
    }

    public int getAlignment() {
        return 0;
    }

    @Environment(EnvType.CLIENT)
    public void setDisplayStrings(String[] strings) {
        this.displayStrings = strings;
    }

    @Environment(EnvType.CLIENT)
    public String[] getDisplayStrings() {
        return displayStrings;
    }
}
