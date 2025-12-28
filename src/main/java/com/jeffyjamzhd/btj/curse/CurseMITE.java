package com.jeffyjamzhd.btj.curse;

import api.world.BlockPos;
import com.jeffyjamzhd.btj.api.curse.AbstractCurseHotbarMeter;
import com.jeffyjamzhd.btj.api.hook.BlockBreakEvent;
import com.jeffyjamzhd.btj.api.hook.FoodConsumedEvent;
import com.jeffyjamzhd.btj.registry.BTJItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/**
 * Curse of MITE, must eat seeds!
 */
public class CurseMITE extends AbstractCurseHotbarMeter implements FoodConsumedEvent, BlockBreakEvent {
    private static final int[] lowrateBiomes = new int[]{
        BiomeGenBase.swampland.biomeID,
        BiomeGenBase.extremeHills.biomeID,
        BiomeGenBase.extremeHillsEdge.biomeID,
        BiomeGenBase.forest.biomeID,
        BiomeGenBase.forestHills.biomeID,
        BiomeGenBase.plains.biomeID
    };

    @Override
    public void init() {
        super.init();
        this.setMaxValue(10000);
        this.setValue(6000);
    }

    @Override
    public void clientInit() {
        super.clientInit();
        this.setDisplayStrings(new String[]{
                "curse.btj.mite.name",
                "curse.btj.mite.desc"
        });
        this.setRenderLocation(0, 0, 9, 9);
    }

    @Override
    public void onTick(World world, EntityPlayer player) {
        super.onTick(world, player);
        this.setValue(Math.max(0, this.getValue() - getDecrement(this.getUpdateCount())));
    }

    @Override
    public void onFoodConsume(EntityPlayer player, ItemStack item) {
        int id = item.getItem().itemID;
        int add = getValueForItem(id);

        this.setValue(this.getValue() + add);
    }

    private int getDecrement(int ticks) {
        if (this.getValue() > 4000)
            return 1;
        else if ((this.getValue() <= 4000 && this.getValue() > 2000) && ticks % 2 == 0)
            return 1;
        else if ((this.getValue() <= 2000) && ticks % 4 == 0)
            return 1;
        return 0;
    }

    private int getValueForItem(int id) {
        if (id == Item.seeds.itemID || id == BTJItems.GRASS_SEED.itemID)
            return 500;
        if (id == Item.pumpkinSeeds.itemID)
            return 1000;
        return 0;
    }

    @Override
    public void onBlockBreak(EntityPlayer player, int block_id, BlockPos pos, int meta) {
        if (!player.worldObj.isRemote && block_id == Block.tallGrass.blockID) {
            World world = player.getEntityWorld();
            Random rand = player.getRNG();
            BiomeGenBase biome = world.getBiomeGenForCoords(pos.x, pos.z);

            // Standard drop (5%)
            if (Arrays.stream(lowrateBiomes).anyMatch(i -> i == biome.biomeID)) {
                if (rand.nextFloat() <= 0.05F) {
                    spawnSeedAt(world, pos);
                }
            // Otherwise (cold/unconventional biomes) higher drop (10%)
            } else if (rand.nextFloat() <= 0.1F) {
                spawnSeedAt(world, pos);
            }
        }
    }

    private void spawnSeedAt(World world, BlockPos pos) {
        ItemStack seed = new ItemStack(BTJItems.GRASS_SEED);
        EntityItem seedEntity = new EntityItem(world, pos.x + 0.5F, pos.y + 0.5F, pos.z + 0.5F, seed);
        seedEntity.delayBeforeCanPickup = 10;
        world.spawnEntityInWorld(seedEntity);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawBar(GuiIngame gui, int width, int height) {
        super.drawBar(gui, width, height);

        // Set shake chance
        float targetChance = (1F - (this.getValue() / 4000F));
        this.shakeChance = this.getValue() < 4000 ? targetChance * targetChance * targetChance : 0F;
    }

    @Override
    public NBTTagCompound writeNBT(NBTTagCompound nbt) {
        nbt = super.writeNBT(nbt);
        nbt.setInteger("seedValue", this.getValue());
        return nbt;
    }

    @Override
    public NBTTagCompound readNBT(NBTTagCompound nbt) {
        nbt = super.readNBT(nbt);
        this.setValue(nbt.getInteger("seedValue"));
        return nbt;
    }

    @Override
    public String getPacketIdentifier() {
        return "btj|curseMiTE";
    }

    @Override
    public void syncToClient(DataOutputStream stream) throws IOException {
        stream.writeInt(this.getValue());
        stream.writeByte(this.updateCount);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void syncFromServer(DataInputStream stream) throws IOException {
        this.setValue(stream.readInt());
        this.updateCount = stream.readByte();
    }
}
