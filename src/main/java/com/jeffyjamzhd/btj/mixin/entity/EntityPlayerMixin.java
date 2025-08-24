package com.jeffyjamzhd.btj.mixin.entity;

import com.jeffyjamzhd.btj.api.CurseManager;
import com.jeffyjamzhd.btj.api.extend.IEntityPlayer;
import net.minecraft.src.EntityLivingBase;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin extends EntityLivingBase implements IEntityPlayer {
    @Shadow public abstract World getEntityWorld();

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

    @Override
    @Unique
    public CurseManager btj$getCurseManager() {
        return this.curseManager;
    }
}
