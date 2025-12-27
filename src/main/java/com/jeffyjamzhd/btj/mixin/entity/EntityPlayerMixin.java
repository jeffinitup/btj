package com.jeffyjamzhd.btj.mixin.entity;

import com.jeffyjamzhd.btj.api.CurseManager;
import com.jeffyjamzhd.btj.api.extend.IEntityPlayer;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin extends EntityLivingBase implements IEntityPlayer {
    @Shadow public abstract World getEntityWorld();

    @Shadow
    public abstract float getArmorExhaustionModifier();

    @Shadow
    public PlayerCapabilities capabilities;
    @Unique
    private CurseManager curseManager;

    public EntityPlayerMixin(World par1World) {
        super(par1World);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void createCurseManager(World world, String str, CallbackInfo ci) {
        this.curseManager = new CurseManager();
    }

    @Inject(method = "onUpdate", at = @At("TAIL"))
    private void curseTickCallback(CallbackInfo ci) {
        this.curseManager.tickCallback(this.getEntityWorld(), ((EntityPlayer) (Object) this));
    }

    @Inject(method = "writeModDataToNBT", at = @At("TAIL"))
    private void curseWriteCallback(NBTTagCompound tag, CallbackInfo ci) {
        this.curseManager.writeNBT(tag, ((EntityPlayer) (Object) this));
    }

    @Inject(method = "readModDataFromNBT", at = @At("HEAD"))
    private void curseReadCallback(NBTTagCompound tag, CallbackInfo ci) {
        this.curseManager.readNBT(tag, ((EntityPlayer) (Object) this));
    }

    @Inject(method = "addExhaustion", at = @At(value = "HEAD"))
    private void addExhaustionCallback(CallbackInfo ci, @Local(argsOnly = true) float exhaustion) {
        if (this.capabilities.isCreativeMode)
            return;
        this.curseManager.addExhaustionCallback((EntityPlayer) (Object) this, exhaustion * this.getArmorExhaustionModifier());
    }

    @Override
    @Unique
    public CurseManager btj$getCurseManager() {
        return this.curseManager;
    }
}
