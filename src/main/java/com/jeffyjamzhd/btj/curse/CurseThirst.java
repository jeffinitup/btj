package com.jeffyjamzhd.btj.curse;

import com.jeffyjamzhd.btj.api.curse.AbstractCurseMeter;
import com.jeffyjamzhd.btj.api.curse.ICurse;
import com.jeffyjamzhd.btj.registry.BTJPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.src.*;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import static com.jeffyjamzhd.btj.BetterThanJosh.LOGGER;

/**
 * Curse of Thirst!
 * Realism at its finest!
 */
public class CurseThirst extends AbstractCurseMeter {
    @Override
    public void init() {
        super.init();
        this.setMaxValue(10000);
        this.setValue(5000);
    }

    @Override
    public void clientInit() {
        super.clientInit();
        this.setDisplayStrings(new String[]{
                "curse.btj.water.name",
                "curse.btj.water.desc"
        });
        this.setRenderLocation(45, 0, 9, 9);
    }

    @Override
    public void onTick(World world, EntityPlayer player) {
        super.onTick(world, player);
        if (!this.hydrate(world, player)) {
            this.setValue(Math.max(0, this.getValue() - 1));
        }
    }

    private boolean hydrate(World world, EntityPlayer player) {
        // Get random
        Random random = player.rand;

        if (player.getAir() <= 0) {
            // When drowning
            this.setValue(Math.max(0, this.getValue() + 100));
            return true;
        } else if (player.isInsideOfMaterial(Material.water)) {
            // When underwater and not drowning
            if (this.getUpdateCount() % 10 == 0) {
                this.setValue(Math.max(0, this.getValue() + 100));

                // Play sound effect
                player.playSound("random.drink", 0.5F + random.nextFloat() * 0.5F, 0.8F + random.nextFloat() * 0.4F);
            }

            // Create bubble particles
            world.spawnParticle(
                    "bubble",
                    player.posX + random.nextDouble() - random.nextDouble(),
                    player.posY + random.nextDouble() - random.nextDouble(),
                    player.posZ + random.nextDouble() - random.nextDouble(),
                    player.motionX,
                    0.5D,
                    player.motionZ
            );
            return true;
        }

        return false;
    }

    @Override
    public void syncToClient(NetServerHandler handler) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        try {
            dataStream.writeInt(this.getValue());
            dataStream.writeByte(this.updateCount);
        } catch (IOException e) {
            LOGGER.trace(e);
        }

        byte[] data = byteStream.toByteArray();
        Packet250CustomPayload packet = new Packet250CustomPayload(BTJPacket.PACKET_CURSE_THIRST_S2C, data);
        handler.sendPacketToPlayer(packet);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void parseSyncPacket(DataInputStream stream) {
        try {
            this.setValue(stream.readInt());
            this.updateCount = stream.readByte();
        } catch (IOException e) {
            LOGGER.trace(e);
        }
    }

    @Override
    public NBTTagCompound writeNBT(NBTTagCompound nbt) {
        nbt = super.writeNBT(nbt);
        nbt.setInteger("ThirstValue", this.getValue());
        return nbt;
    }

    @Override
    public NBTTagCompound readNBT(NBTTagCompound nbt) {
        nbt = super.readNBT(nbt);
        this.setValue(nbt.getInteger("ThirstValue"));
        return nbt;
    }

    @Override
    public ICurse createInstance() {
        CurseThirst curse = new CurseThirst();
        curse.setIdentifier(this.getIdentifier());
        return curse;
    }
}
