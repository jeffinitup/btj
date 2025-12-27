package com.jeffyjamzhd.btj.registry;

import com.jeffyjamzhd.btj.BetterThanJosh;
import com.jeffyjamzhd.btj.api.CurseRegistry;
import com.jeffyjamzhd.btj.api.curse.AbstractCurse;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Packet250CustomPayload;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.List;
import java.util.Map;

import static com.jeffyjamzhd.btj.BetterThanJosh.LOGGER;

public class BTJPacket {
    public static String PACKET_CURSE_S2C = "btj|curseList";

    public static void register(BetterThanJosh mod) {
        // Curse list sync packet
        mod.registerPacketHandler(PACKET_CURSE_S2C, (payload, player) -> {
            DataInputStream stream = new DataInputStream(new ByteArrayInputStream(payload.data));
            player.btj$getCurseManager().parseLocalList(stream);
        });
    }

    /**
     * Register packet stuff for each BTJ curse
     */
    public static void postRegister(BetterThanJosh mod) {
        LOGGER.info("Registering packets for BTJ curses");
        for (Map.Entry<String, Class<? extends AbstractCurse>> entry :
                CurseRegistry.INSTANCE.getCurses().entrySet()) {
            try {
                // Split entry into individual variables
                String curseID = entry.getKey();
                Class<? extends AbstractCurse> curseClass = entry.getValue();

                // Skip over non BTJ namespaced curses
                if (!curseID.startsWith("btj"))
                    continue;

                // Create curse object, skip if it doesn't use standard packet system
                AbstractCurse curse = curseClass.getConstructor().newInstance();
                if (!curse.usePacketSystem())
                    continue;

                // Register sync packet associated with curse
                String packetID = curse.getPacketIdentifier();
                mod.registerPacketHandler(packetID,
                        (payload, player) -> genericCursePayload(payload, player, curseID));
                LOGGER.info("Packet registered: {}", packetID);
            } catch (Exception e) {
                LOGGER.error("Something went wrong when parsing curses for packet registration!");
                LOGGER.trace(e.getMessage());
            }
        }
    }

    private static void genericCursePayload(Packet250CustomPayload payload, EntityPlayer player, String identifier) {
        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(payload.data));
        List<AbstractCurse> curses = player.btj$getCurseManager().getCurses();
        for (AbstractCurse curse : curses) {
            if (curse.getIdentifier().equals(identifier)) {
                curse.syncFromServerWrapper(stream);
                break;
            }
        }
    }
}
