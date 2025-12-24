package com.jeffyjamzhd.btj.registry;

import api.util.AddonSoundRegistryEntry;
import com.jeffyjamzhd.btj.BetterThanJosh;

public class BTJSound {
    public static AddonSoundRegistryEntry CURSE;

    public static void register() {
        CURSE = new AddonSoundRegistryEntry(BetterThanJosh.idOfString("misc.curse"));
    }
}
