package com.jeffyjamzhd.btj.item;

import com.jeffyjamzhd.btj.registry.BTJItems;
import net.minecraft.src.*;

public class BowlItem extends Item {
    public BowlItem(int id) {
        super(id);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        MovingObjectPosition movingObjectPosition = this.getMovingObjectPositionFromPlayer(world, player, true);
        if (movingObjectPosition == null) {
            return stack;
        }

        if (movingObjectPosition.typeOfHit == EnumMovingObjectType.TILE) {
            int x = movingObjectPosition.blockX;
            int y = movingObjectPosition.blockY;
            int z = movingObjectPosition.blockZ;
            if (!world.canMineBlock(player, x, y, z)) {
                return stack;
            }

            if (!player.canPlayerEdit(x, y, z, movingObjectPosition.sideHit, stack)) {
                return stack;
            }
            if (world.getBlockMaterial(x, y, z) == Material.water) {
                if (--stack.stackSize <= 0) {
                    return new ItemStack(BTJItems.BOWL_OF_WATER);
                }
                if (!player.inventory.addItemStackToInventory(new ItemStack(BTJItems.BOWL_OF_WATER))) {
                    player.dropPlayerItem(new ItemStack(BTJItems.BOWL_OF_WATER, 1, 0));
                }
                return stack;
            }
        }

        return super.onItemRightClick(stack, world, player);
    }
}
