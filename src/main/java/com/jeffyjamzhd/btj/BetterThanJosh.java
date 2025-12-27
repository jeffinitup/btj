package com.jeffyjamzhd.btj;

import api.BTWAddon;
import com.jeffyjamzhd.btj.api.registry.RegistryEventCurse;
import com.jeffyjamzhd.btj.command.CommandCurse;
import com.jeffyjamzhd.btj.registry.*;
import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.minecraft.src.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BetterThanJosh extends BTWAddon {
    public static final Logger LOGGER = LogManager.getLogger(BetterThanJosh.class);
    private static BetterThanJosh INSTANCE;

    public BetterThanJosh() {
        super();
        INSTANCE = this;
    }

    @Override
    public void preInitialize() {
        MixinExtrasBootstrap.init();
    }

    @Override
    public void initialize() {
        LOGGER.info("{} Version {} Initializing...", this.getName(), this.getVersionString());

        RegistryEventCurse.register(new BTJCurses());
        registerAddonCommand(new CommandCurse());

        BTJItems.register();
        BTJPacket.register(this);
        BTJSound.register();
        BTJRecipes.register();
        BTJStatusEffects.createStatuses();

        LOGGER.info("{} Initialized.", this.getName());
    }

    @Override
    public void postInitialize() {
        LOGGER.info("Sending out register events");

        RegistryEventCurse.init();
        BTJPacket.postRegister(this);
    }

    public String getPrettyVerisonString() {
        return getName() + " v" + getVersionString();
    }

    public static BetterThanJosh getInstance() {
        return INSTANCE;
    }

    public static ResourceLocation idOf(String path) {
        return new ResourceLocation(INSTANCE.getModID(), path);
    }

    public static String idOfString(String path) {
        return INSTANCE.getModID() + ":" + path;
    }
}