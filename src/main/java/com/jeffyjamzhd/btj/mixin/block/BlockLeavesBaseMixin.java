package com.jeffyjamzhd.btj.mixin.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.SERVER)
@Mixin(BlockLeavesBase.class)
public abstract class BlockLeavesBaseMixin extends Block {
    @Shadow
    protected float movementModifier;

    protected BlockLeavesBaseMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    /**
     * @author jeffyjamzhd
     * @reason Broken on server side, needs fix
     */
    @Overwrite
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (!(entity instanceof IProjectile)) {
            if (entity.isAffectedByMovementModifiers()) {
                entity.motionX *= this.movementModifier;
                entity.motionZ *= this.movementModifier;
                if (entity.motionY < 0.0F) {
                    entity.motionY *= this.movementModifier;
                }
            }

            entity.fallDistance = 0.0F;
        }
    }
}
