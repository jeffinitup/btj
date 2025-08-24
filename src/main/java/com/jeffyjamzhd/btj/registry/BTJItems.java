package com.jeffyjamzhd.btj.registry;

import btw.AddonHandler;
import com.jeffyjamzhd.btj.BetterThanJosh;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemFood;

import static com.jeffyjamzhd.btj.BetterThanJosh.LOGGER;

public class BTJItems {
    public static final int ID_BASE = 31500;
    public static ItemFood GRASS_SEED;

    public static void register() {
        LOGGER.info("Registering items");

        GRASS_SEED = (ItemFood) new ItemFood(ID_BASE, 0, 0, false, false)
                .setAlwaysEdible()
                .setMaxStackSize(32)
                .setTextureName(BetterThanJosh.idOfString("grass_seed"))
                .setUnlocalizedName("grassSeed")
                .setCreativeTab(CreativeTabs.tabFood);
    }
}
