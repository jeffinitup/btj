package com.jeffyjamzhd.btj.registry;

import api.util.AddonSoundRegistryEntry;
import com.jeffyjamzhd.btj.BetterThanJosh;

public class BTJSound {
    public static AddonSoundRegistryEntry CURSE;
    public static AddonSoundRegistryEntry GARGLE;

    public static void register() {
        CURSE = new AddonSoundRegistryEntry(BetterThanJosh.idOfString("misc.curse"));
        GARGLE = new AddonSoundRegistryEntry(BetterThanJosh.idOfString("misc.gargle"));
    }
}
