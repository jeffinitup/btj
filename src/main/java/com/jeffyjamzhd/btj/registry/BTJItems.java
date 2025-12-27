package com.jeffyjamzhd.btj.registry;

import btw.item.items.SoupItem;
import com.jeffyjamzhd.btj.BetterThanJosh;
import com.jeffyjamzhd.btj.item.BowlItem;
import com.jeffyjamzhd.btj.item.SoupWaterItem;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemFood;
import net.minecraft.src.Item;

import static com.jeffyjamzhd.btj.BetterThanJosh.LOGGER;

public class BTJItems {
    public static final int ID_BASE = 31500;

    public static final ItemFood GRASS_SEED = (ItemFood) new ItemFood(ID_BASE, 0, 0, false, false)
            .setAlwaysEdible()
            .setMaxStackSize(32)
            .setTextureName(BetterThanJosh.idOfString("grass_seed"))
            .setUnlocalizedName("grassSeed")
            .setCreativeTab(CreativeTabs.tabFood);
    public static final SoupItem BOWL_OF_WATER = (SoupItem) new SoupWaterItem(ID_BASE + 1)
            .setAlwaysEdible()
            .setMaxStackSize(1)
            .setContainerItem(Item.bowlEmpty)
            .setTextureName(BetterThanJosh.idOfString("bowl_of_water"))
            .setUnlocalizedName("bowlOfWater")
            .setCreativeTab(CreativeTabs.tabFood);


    public static void register() {
        LOGGER.info("Registering items");
    }
}
