package com.jeffyjamzhd.btj;

import btw.AddonHandler;
import btw.BTWAddon;
import com.jeffyjamzhd.btj.api.CurseDisplayManager;
import com.jeffyjamzhd.btj.api.event.curse.EventCurse;
import com.jeffyjamzhd.btj.command.CommandCurse;
import com.jeffyjamzhd.btj.registry.BTJCurses;
import net.minecraft.src.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class BetterThanJosh extends BTWAddon {
    public static final Logger LOGGER = LogManager.getLogger(BetterThanJosh.class);
    private static BetterThanJosh INSTANCE;

    public BetterThanJosh() {
        super();
        INSTANCE = this;
    }

    @Override
    public void initialize() {
        LOGGER.info("{} Version {} Initializing...", this.getName(), this.getVersionString());

        EventCurse.register(new BTJCurses());
        registerAddonCommand(new CommandCurse());
        registerPacketHandler(this.getModID() + "|curseList", (payload, player) -> {
            DataInputStream stream = new DataInputStream(new ByteArrayInputStream(payload.data));
            player.btj$getCurseManager().parseLocalList(stream);
        });

        LOGGER.info("{} Initialized.", this.getName());
    }

    @Override
    public void postInitialize() {
        LOGGER.info("Sending out register events");
        EventCurse.init();
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