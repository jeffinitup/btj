package com.jeffyjamzhd.btj.curse;

import com.jeffyjamzhd.btj.api.curse.AbstractCurseMeter;
import com.jeffyjamzhd.btj.api.curse.ICurse;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.ResourceLocation;

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
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            this.setRenderLocation(45, 0, 9, 9);
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
