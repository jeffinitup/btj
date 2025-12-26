package com.jeffyjamzhd.btj.curse;

import com.jeffyjamzhd.btj.api.curse.AbstractCurseMeter;
import com.jeffyjamzhd.btj.api.curse.ICurse;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.src.*;

import java.util.Random;

/**
 * Curse of Thirst!
 * Realism at its finest!
 */
public class CurseThirst extends AbstractCurseMeter {
    public CurseThirst(ResourceLocation texture) {
        super(texture);
        this.setDisplayStrings(new String[]{
                "curse.btj.water.name",
                "curse.btj.water.desc"
        });
        this.setMaxValue(10000);
        this.setValue(5000);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
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
        if (player.getAir() <= 0) {
            this.setValue(Math.max(0, this.getValue() + 100));
            return true;
        } else if (player.isInsideOfMaterial(Material.water)) {
            if (this.getUpdateCount() % 10 == 0) {
                this.setValue(Math.max(0, this.getValue() + 100));
            }

            Random random = new Random();
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
        CurseThirst curse = new CurseThirst(this.getTexture());
        curse.setIdentifier(this.getIdentifier());
        return curse;
    }
}
