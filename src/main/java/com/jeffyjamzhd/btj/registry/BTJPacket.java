package com.jeffyjamzhd.btj.registry;

import com.jeffyjamzhd.btj.BetterThanJosh;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class BTJPacket {
    public static String PACKET_CURSE_S2C = "btj|curseList";

    public static void register(BetterThanJosh mod) {
        mod.registerPacketHandler(PACKET_CURSE_S2C, (payload, player) -> {
            DataInputStream stream = new DataInputStream(new ByteArrayInputStream(payload.data));
            player.btj$getCurseManager().parseLocalList(stream);
        });
    }
}
