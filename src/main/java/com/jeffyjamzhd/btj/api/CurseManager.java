package com.jeffyjamzhd.btj.api;

import com.jeffyjamzhd.btj.BetterThanJosh;
import com.jeffyjamzhd.btj.api.curse.ICurse;
import net.minecraft.src.*;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.jeffyjamzhd.btj.BetterThanJosh.LOGGER;

/**
 * Manages callbacks for curses a player currently has
 */
public class CurseManager {
    private List<ICurse> curses = new ArrayList<>();
    private boolean dirty = false;

    public void tickCallback(World world, EntityPlayer player) {
        if (this.dirty && player instanceof EntityPlayerMP mp) {
            sendSyncPacketToClient(mp.playerNetServerHandler, mp);
            this.dirty = false;
        }

        for (ICurse curse : this.curses)
            curse.onTick(world, player);
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
                    ICurse curse = this.applyCurse(identifier, player);

                    // Check null
                    if (curse == null) {
                        LOGGER.error("Curse is no longer registered - {}", identifier);
                        continue;
                    }

                    // Read nbt
                    curse.readNBT(curseTag);
                }
            }
            this.dirty = true;
        }
    }

    /**
     * Adds a curse to manager (silently)
     * @param id Curse identifier
     * @return Curse applied
     */
    private ICurse applyCurse(String id, EntityPlayer player) {
        ICurse curse = CurseRegistry.INSTANCE.getCurse(id);
        boolean condition = this.curses.stream().anyMatch(iCurse -> iCurse.getIdentifier().equals(id));
        if (!condition && curse != null) {
            this.curses.add(curse);
            if (player instanceof EntityPlayerMP mp)
                sendSyncPacketToClient(mp.playerNetServerHandler, mp);
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
        ICurse curse = applyCurse(id, player);
        if (curse != null) {
            curse.onActivation(world, player);
            return true;
        }
        LOGGER.error("Malformed identifier provided or curse already exists in player - {}", id);
        return false;
    }

    public boolean removeCurse(String id, World world, EntityPlayer player) {
        ICurse curse = CurseRegistry.INSTANCE.getCurse(id);
        if (curse != null) {
            curse.onDeactivation(world, player);
            boolean result = this.curses.remove(curse);
            if (player instanceof EntityPlayerMP mp)
                sendSyncPacketToClient(mp.playerNetServerHandler, mp);
            return result;
        }
        LOGGER.error("Malformed identifier provided or curse doesnt exist in player - {}", id);
        return false;
    }

    /**
     * Gets active curses
     * @return List of curses
     */
    public List<ICurse> getCurses() {
        return this.curses;
    }

    /**
     * Syncs curses with client
     * @param handler Net handler
     * @param player Player
     */
    private void sendSyncPacketToClient(NetServerHandler handler, EntityPlayerMP player) {
        if (handler == null)
            return;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        try {
            // Write size of curse id and curse id
            for (ICurse curse : curses) {
                dataStream.writeByte(curse.getIdentifier().length());
                dataStream.writeBytes(curse.getIdentifier());
            }
        } catch (IOException e) {
            LOGGER.trace(e);
        }

        byte[] data = byteStream.toByteArray();
        Packet250CustomPayload packet = new Packet250CustomPayload("btj|curseList", data);
        handler.sendPacketToPlayer(packet);
    }

    /**
     * Parses list of curses on the client side
     * @param stream Data
     */
    public void parseLocalList(DataInputStream stream) {
        byte len;
        try {
            this.curses = new ArrayList<>();
            while ((len = stream.readByte()) >= 0) {
                String curse = new String(stream.readNBytes(len), StandardCharsets.US_ASCII);
                this.applyCurse(curse, null);
            }
        } catch (IOException e) {
            LOGGER.trace(e);
        }
    }
}
