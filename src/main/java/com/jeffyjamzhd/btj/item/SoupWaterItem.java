package com.jeffyjamzhd.btj.item;

import btw.item.items.SoupItem;
import net.minecraft.src.EnumAction;
import net.minecraft.src.ItemStack;

public class SoupWaterItem extends SoupItem {
    public SoupWaterItem(int iItemID) {
        super(iItemID, 0, 0, false, "");
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack) {
        return EnumAction.drink;
    }
}
