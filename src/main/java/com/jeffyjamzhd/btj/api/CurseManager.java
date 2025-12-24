package com.jeffyjamzhd.btj.api;

import api.world.BlockPos;
import com.jeffyjamzhd.btj.api.curse.ICurse;
import com.jeffyjamzhd.btj.api.curse.IPlayerEvents;
import com.jeffyjamzhd.btj.registry.BTJPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.jeffyjamzhd.btj.BetterThanJosh.LOGGER;

/**
 * Manages callbacks for curses a player currently has
 */
public class CurseManager {
    private final List<ICurse> curses = new ArrayList<>();
    private boolean dirty = false;
    private boolean coldLoad = true;

    /**
     * Called upon each tick
     */
    public void tickCallback(World world, EntityPlayer player) {
        // Send sync packet if marked as dirty
        if (this.dirty && player instanceof EntityPlayerMP mp) {
            sendSyncPacketToClient(mp.playerNetServerHandler);
            this.dirty = false;
        }

        // Tick curses
        for (ICurse curse : this.curses)
            if (!player.capabilities.isCreativeMode)
                curse.onTick(world, player);
    }

    /**
     * Called upon food consumption
     */
    public void consumeFoodCallback(EntityPlayer player, ItemStack food) {
        for (ICurse curse : this.curses)
            if (curse instanceof IPlayerEvents cursePE && !player.capabilities.isCreativeMode)
                cursePE.onFoodConsume(player, food);
    }

    /**
     * Called upon successful block placement
     */
    public void blockPlaceCallback(EntityPlayer player, Block block) {
        for (ICurse curse : this.curses)
            if (curse instanceof IPlayerEvents cursePE && !player.capabilities.isCreativeMode)
                cursePE.onBlockPlace(player, block);
    }

    /**
     * Called upon successful block break
     */
    public void blockBrokenCallback(EntityPlayer player, int block_id, BlockPos pos, int meta) {
        for (ICurse curse : this.curses)
            if (curse instanceof IPlayerEvents cursePE)
                cursePE.onBlockBreak(player, block_id, pos, meta);
    }

    public void writeNBT(NBTTagCompound compound, EntityPlayer player) {
        // Nest curse nbt into curses compound
        LOGGER.info("Writing curses to player {}", player.getCommandSenderName());
        NBTTagList nbtCurses = new NBTTagList("BTJCurse");
        for (ICurse curse : this.curses) {
            // Write in nested nbt
            LOGGER.info("Writing curse {} to player {}", curse.getIdentifier(), player.getCommandSenderName());
            NBTTagCompound data = new NBTTagCompound();
            data.setString("Identifier", curse.getIdentifier());
            nbtCurses.appendTag(curse.writeNBT(data));
        }
        compound.setTag("BTJCurse", nbtCurses);
    }

    public void readNBT(NBTTagCompound compound, EntityPlayer player) {
        LOGGER.info("Reading curses from player {}", player.getCommandSenderName());
        if (compound.hasKey("BTJCurse")) {
            // Get curses and iterate through tags
            NBTTagList nbtCurses = compound.getTagList("BTJCurse");
            for (int id = 0; id < nbtCurses.tagCount(); id++) {
                NBTBase tag = nbtCurses.tagAt(id);
                if (tag instanceof NBTTagCompound curseTag) {
                    // Tag compound is a curse
                    LOGGER.info("Reading curse {} from player {}", curseTag.getString("Identifier"), player.getCommandSenderName());
                    String identifier = curseTag.getString("Identifier");
                    ICurse curse = this.applyCurseSilent(identifier, player);

                    // Check null
                    if (curse == null) {
                        LOGGER.error("Curse is no longer registered - {}", identifier);
                        continue;
                    }

                    // Read nbt
                    curse.readNBT(curseTag);
                }
            }
        }
        // Boy this dirty clean this up
        this.dirty = true;
    }

    /**
     * Adds a curse to manager (silently)
     * @param id Curse identifier
     * @return Curse applied
     */
    private ICurse applyCurseSilent(String id, EntityPlayer player) {
        ICurse curse = CurseRegistry.INSTANCE.getCurse(id);
        boolean condition = this.curses.stream().anyMatch(iCurse -> iCurse.getIdentifier().equals(id));
        if (!condition && curse != null) {
            curse = curse.createInstance();
            this.curses.add(curse);
            if (player instanceof EntityPlayerMP mp) {
                sendSyncPacketToClient(mp.playerNetServerHandler);
            }
            return curse;
        }
        return null;
    }

    /**
     * Adds a curse to player
     * @param id Curse identifier
     * @param world World
     * @param player Player entity
     * @return If the curse was successfully added
     */
    public boolean applyCurse(String id, World world, EntityPlayer player) {
        ICurse curse = applyCurseSilent(id, player);
        if (curse != null) {
            curse.onActivation(world, player);
            if (world.isRemote) {
                Minecraft.getMinecraft().ingameGUI.btj$getCurseDisplay().beginRenderingCurse(curse);
            }
            return true;
        }
        LOGGER.error("Malformed identifier provided or curse already exists in player - {}", id);
        return false;
    }

    /**
     * Removes curse from player
     * @param id Curse identifier
     * @param world World
     * @param player Player entity
     * @return If the curse was successfully removed
     */
    public boolean removeCurse(String id, World world, EntityPlayer player) {
        for (ICurse curse : this.curses) {
            if (curse.getIdentifier().equals(id)) {
                curse.onDeactivation(world, player);
                boolean result = this.curses.remove(curse);
                if (player instanceof EntityPlayerMP mp)
                    sendSyncPacketToClient(mp.playerNetServerHandler);
                return result;
            }
        }
        LOGGER.error("Malformed identifier provided or curse doesnt exist in player - {}", id);
        return false;
    }

    /**
     * Checks if a curse is currently within this CurseManager
     * @param id Curse identifier
     */
    public boolean hasCurse(String id) {
        return this.curses.stream()
                .anyMatch(iCurse -> iCurse.getIdentifier().equals(id));
    }

    /**
     * Checks if a curse is currently within this CurseManager
     * @param curse Registered curse
     */
    public boolean hasCurse(ICurse curse) {
        return this.curses.stream()
                .anyMatch(iCurse -> iCurse.getIdentifier().equals(curse.getIdentifier()));
    }

    /**
     * Gets active curses
     * @return List of curses
     */
    public List<ICurse> getCurses() {
        return this.curses;
    }

    /**
     * Gets curse instance with provided identifier
     * @param id Curse identifier
     * @return Curse instance
     */
    public Optional<ICurse> getCurse(String id) {
        return this.curses.stream()
                .filter(iCurse -> iCurse.getIdentifier().equals(id))
                .findFirst();
    }

    /**
     * Gets curse instance with provided curse registry
     * @param curse Registered curse
     * @return Curse instance
     */
    public Optional<ICurse> getCurse(ICurse curse) {
        return this.curses.stream()
                .filter(iCurse -> iCurse.getIdentifier().equals(curse.getIdentifier()))
                .findFirst();
    }

    /**
     * Syncs curses with client
     * @param handler Net handler
     * @see Packet250CustomPayload
     */
    private void sendSyncPacketToClient(NetServerHandler handler) {
        if (handler == null)
            return;

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        try {
            // Write if this is a first load
            dataStream.writeByte(this.coldLoad ? 1 : 0);
            this.coldLoad = false;

            // Write size of curse id and curse id
            for (ICurse curse : curses) {
                dataStream.writeByte(curse.getIdentifier().length());
                dataStream.writeBytes(curse.getIdentifier());
            }
            dataStream.writeByte(Byte.MIN_VALUE);
        } catch (IOException e) {
            LOGGER.trace(e);
        }

        byte[] data = byteStream.toByteArray();
        Packet250CustomPayload packet = new Packet250CustomPayload(BTJPacket.PACKET_CURSE_S2C, data);
        handler.sendPacketToPlayer(packet);
    }

    /**
     * Parses list of curses on the client side
     * @param stream Data
     */
    @Environment(EnvType.CLIENT)
    public void parseLocalList(DataInputStream stream) {
        byte len;
        try {
            // Clear old curse list
            List<ICurse> old = new ArrayList<>(this.curses);
            this.curses.clear();

            // Get status of load
            boolean isColdLoad = stream.readByte() == 1;

            // Iterate and apply curses on client
            EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
            while ((len = stream.readByte()) != Byte.MIN_VALUE) {
                String curse = new String(stream.readNBytes(len), StandardCharsets.US_ASCII);
                if (!isColdLoad && old.stream().noneMatch(iCurse -> iCurse.getIdentifier().equals(curse)))
                    this.applyCurse(curse, player.worldObj, player);
                else if (old.stream().anyMatch(iCurse -> iCurse.getIdentifier().equals(curse))) {
                    Optional<ICurse> existing = old.stream()
                            .filter(iCurse -> iCurse.getIdentifier().equals(curse))
                            .findFirst();
                    existing.ifPresent(this.curses::add);
                } else {
                    this.applyCurseSilent(curse, player);
                }
            }
        } catch (IOException e) {
            LOGGER.trace(e);
        }
    }
}
