package com.jeffyjamzhd.btj.mixin.network;

import com.jeffyjamzhd.btj.api.curse.AbstractCurse;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ServerConfigurationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Shadow private ServerConfigurationManager serverConfigManager;

    @Inject(method = "stopServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ServerConfigurationManager;removeAllPlayers()V"))
    private void deactivateAllCurses(CallbackInfo ci) {
        for (Object object : this.serverConfigManager.playerEntityList) {
            if (object instanceof EntityPlayer player) {
                List<AbstractCurse> curses = player.btj$getCurseManager().getCurses();
                for (AbstractCurse curse : curses)
                    curse.onDeactivation(player.worldObj, player);
            }
        }
    }
}
