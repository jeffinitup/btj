package com.jeffyjamzhd.btj.registry;

import com.jeffyjamzhd.btj.BetterThanJosh;
import com.jeffyjamzhd.btj.api.curse.ICurse;
import com.jeffyjamzhd.btj.curse.CurseMITE;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Packet250CustomPayload;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.List;

public class BTJPacket {
    public static String PACKET_CURSE_S2C = "btj|curseList";
    public static String PACKET_CURSE_MITE_S2C = "btj|curseMiTE";

    public static void register(BetterThanJosh mod) {
        mod.registerPacketHandler(PACKET_CURSE_S2C, (payload, player) -> {
            DataInputStream stream = new DataInputStream(new ByteArrayInputStream(payload.data));
            player.btj$getCurseManager().parseLocalList(stream);
        });
        mod.registerPacketHandler(PACKET_CURSE_MITE_S2C, (payload, player) ->
                genericCursePayload(payload, player, "btj:mite"));
    }

    private static void genericCursePayload(Packet250CustomPayload payload, EntityPlayer player, String identifier) {
        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(payload.data));
        List<ICurse> curses = player.btj$getCurseManager().getCurses();
        for (ICurse curse : curses) {
            if (curse.getIdentifier().equals(identifier)) {
                curse.parseSyncPacket(stream);
                break;
            }
        }
    }
}
