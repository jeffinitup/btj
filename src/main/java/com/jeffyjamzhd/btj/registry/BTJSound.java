package com.jeffyjamzhd.btj.registry;

import btw.util.sounds.AddonSoundRegistryEntry;
import com.jeffyjamzhd.btj.BetterThanJosh;

public class BTJSound {
    public static AddonSoundRegistryEntry CURSE;

    public static void register() {
        CURSE = new AddonSoundRegistryEntry(BetterThanJosh.idOfString("misc.curse"));
    }
}
