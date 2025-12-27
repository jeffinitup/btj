package com.jeffyjamzhd.btj.curse;

import com.jeffyjamzhd.btj.api.curse.AbstractCurseMeter;
import com.jeffyjamzhd.btj.api.curse.ICurse;
import com.jeffyjamzhd.btj.api.hook.ExhaustionEvent;
import com.jeffyjamzhd.btj.api.hook.FoodConsumedEvent;
import com.jeffyjamzhd.btj.registry.BTJItems;
import com.jeffyjamzhd.btj.registry.BTJPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import static com.jeffyjamzhd.btj.BetterThanJosh.LOGGER;

/**
 * Curse of Thirst!
 * Realism at its finest!
 */
public class CurseThirst extends AbstractCurseMeter implements FoodConsumedEvent, ExhaustionEvent {
    private static final HashMap<Integer, Integer> HYDRATION_VALUES = new HashMap<>();

    @Override
    public void init() {
        super.init();
        this.setMaxValue(10000);
        this.setValue(8000);
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
        this.hydrate(world, player);
    }

    @Override
    public void onFoodConsume(EntityPlayer player, ItemStack item) {
        int hydration = HYDRATION_VALUES.getOrDefault(item.itemID, 0);
        if (hydration != 0) {
            this.setValue(Math.max(0, this.getValue() + hydration));
        }
    }

    @Override
    public void onExhaustion(EntityPlayer player, float exhaustion) {
        int targetExhaustion = Math.min((int) (exhaustion * 300), 150);
        this.setValue(Math.max(0, this.getValue() - targetExhaustion));
        if (player.isClientWorld())
            this.setShakeTicks((int) (exhaustion * 30));
    }

    /**
     * Ambient water drinking function
     */
    private void hydrate(World world, EntityPlayer player) {
        // Get random
        Random random = player.rand;

        if (player.getAir() <= 0) {
            // When drowning
            this.setValue(Math.max(0, this.getValue() + 100));
        } else if (player.isInsideOfMaterial(Material.water) && this.getValue() < this.getMaxValue() - 200) {
            // When underwater and not drowning
            if (this.getUpdateCount() % 20 == 0) {
                this.setValue(Math.max(0, this.getValue() + 200));

                // Cosmetic stuff
                this.spawnParticles(player, world);
                player.playSound("random.drink", 0.5F + random.nextFloat() * 0.5F, 0.8F + random.nextFloat() * 0.4F);
            }
        }

    }

    /**
     * Spawns bubble particles infront of the player's face
     */
    private void spawnParticles(EntityPlayer player, World world) {
        Vec3 velVec = world.getWorldVec3Pool().getVecFromPool(((double)world.rand.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
        velVec.rotateAroundX(-player.rotationPitch * (float)Math.PI / 180.0f);
        velVec.rotateAroundY(-player.rotationYaw * (float)Math.PI / 180.0f);
        Vec3 posVec = world.getWorldVec3Pool().getVecFromPool(((double)world.rand.nextFloat() - 0.5) * 0.3, (double)(-world.rand.nextFloat()) * 0.6 - 0.3, 0.6);
        posVec.rotateAroundX(-player.rotationPitch * (float)Math.PI / 180.0f);
        posVec.rotateAroundY(-player.rotationYaw * (float)Math.PI / 180.0f);
        posVec = posVec.addVector(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
        for (int i = 0; i < 3; i++) {
            Vec3 varVec = world.getWorldVec3Pool().getVecFromPool(((double)world.rand.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
            varVec.rotateAroundX(-player.rotationPitch * (float)Math.PI / 180.0f);
            varVec.rotateAroundY(-player.rotationYaw * (float)Math.PI / 180.0f);
            varVec = varVec.addVector(velVec);
            world.spawnParticle("bubble",
                    posVec.xCoord, posVec.yCoord, posVec.zCoord,
                    varVec.xCoord, varVec.yCoord + 0.3, varVec.zCoord);
        }
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
    public void drawBar(GuiIngame gui, int width, int height) {
        super.drawBar(gui, width, height);

        // Set shake chance
        float targetChance = (1F - (this.getValue() / 4000F));
        this.shakeChance = this.getValue() < 4000 ? targetChance * targetChance * targetChance : 0F;
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

    static {
        for (Item item : Item.itemsList) {
            // Soups give minimal hydration
            if (item instanceof ItemSoup) {
                HYDRATION_VALUES.put(item.itemID, 500);
            }
        }

        HYDRATION_VALUES.put(Item.potion.itemID, 2000);
        HYDRATION_VALUES.put(BTJItems.BOWL_OF_WATER.itemID, 4000);
        HYDRATION_VALUES.put(Item.bread.itemID, -1000);
        HYDRATION_VALUES.put(288, -8000); // Battotest baguette
    }
}
